/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2023.                            (c) 2023.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 *  : 5 $
 *
 ************************************************************************
 */

package org.opencadc.erfa;

import ca.nrc.cadc.util.Log4jInit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * Each of the transformation tests replicates the original C ERFA test.
 */
public class ERFALibTest {

    private static final Logger log = Logger.getLogger(ERFALibTest.class);

    static
    {
        // debug only adds a few lines from NativeUtil deploying the shared lib
        Log4jInit.setLevel("org.opencadc.erfa", Level.INFO);
    }

    public ERFALibTest() {
    }

    @Test
    public void tt2utcRoundTripTest() {
        try {
            double tt1 = 2453750.5;
            double tt2 = 0.892104561;
            double dt = 64.8499;
            double dut1 = 0.3341;

            double[] utc = ERFALib.tt2utc(tt1, tt2, dt, dut1);
            log.debug(String.format("tt[%s, %s] -> utc[%s, %s]", tt1, tt2, utc[0], utc[1]));

            double[] tt = ERFALib.utc2tt(utc[0], utc[1], dut1, dt);
            log.debug(String.format("utc[%s, %s] -> tt[%s, %s]", utc[0], utc[1], tt[0], tt[1]));

            Assert.assertEquals(tt1, tt[0], 1e-6);
            Assert.assertEquals(tt2, tt[1], 1e-12);

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void utc2ttRoundTripTest() {
        try {
            double utc1 = 2453750.5;
            double utc2 = 0.892100694;
            double dt = 64.8499;
            double dut1 = 0.3341;

            double[] tt = ERFALib.utc2tt(utc1, utc2, dut1, dt);
            log.debug(String.format("utc[%s, %s] -> tt[%s, %s]", utc1, utc2, tt[0], tt[1]));

            double[] utc = ERFALib.tt2utc(tt[0], tt[1], dt, dut1);
            log.debug(String.format("tt[%s, %s] -> utc[%s, %s]", utc[0], utc[1], tt[0], tt[1]));

            Assert.assertEquals(utc1, utc[0], 1e-6);
            Assert.assertEquals(utc2, utc[1], 1e-12);

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void tai2ttTest() {
        try {
            double tai1 = 2453750.5;
            double tai2 = 0.892482639;
            double[] expected = {2453750.5, 0.892855139};

            double[] actual = ERFALib.tai2tt(tai1, tai2);
            log.debug(String.format("tai[%s, %s] -> tt[%s, %s]", tai1, tai2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);
        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void tai2ut1Test() {
        try {
            double tai1 = 2453750.5;
            double tai2 = 0.892482639;
            double dta = -32.6659;
            double[] expected = {2453750.5, 0.8921045614537037037};

            double[] actual = ERFALib.tai2ut1(tai1, tai2, dta);
            log.debug(String.format("tai[%s, %s] -> ut1[%s, %s]", tai1, tai2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);
        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void tai2utcTest() {
        try {
            double tai1 = 2453750.5;
            double tai2 = 0.892482639;
            double[] expected = {2453750.5, 0.8921006945555555556};

            double[] actual = ERFALib.tai2utc(tai1, tai2);
            log.debug(String.format("tai[%s, %s] -> utc[%s, %s]", tai1, tai2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);

            // UTC began at 1960 January 1 (JD 2436934.5), dates previous
            // should throw a DubiousYearException
            try {
                double dubious = 2436930.5;
                ERFALib.tai2utc(dubious, 0.0);
                Assert.fail("DubiousYearException expected");
            } catch (DubiousYearException e) {
                // expected
            }

            // dates before January 1 -4799 (JD -31738.5)
            // should throw an UnacceptableDateException
            try {
                double unacceptable = -31739.5;
                ERFALib.tai2utc(unacceptable, 0.0);
                Assert.fail("UnacceptableDateException expected");
            } catch (UnacceptableDateException e) {
                // expected
            }

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void tt2taiTest() {
        try {
            double tt1 = 2453750.5;
            double tt2 = 0.892482639;
            double[] expected = {2453750.5, 0.892110139};

            double[] actual = ERFALib.tt2tai(tt1, tt2);
            log.debug(String.format("tt[%s, %s] -> tai[%s, %s]", tt1, tt2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);
        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void tt2ut1Test() {
        try {
            double tt1 = 2453750.5;
            double tt2 = 0.892104561;
            double dt = -32.6659;
            double[] expected = {2453750.5, 0.8924826385462962963};

            double[] actual = ERFALib.tt2ut1(tt1, tt2, dt);
            log.debug(String.format("tt[%s, %s] -> ut1[%s, %s]", tt1, tt2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);
        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void ut12taiTest() {
        try {
            double ut11 = 2453750.5;
            double ut12 = 0.892104561;
            double dta = -32.6659;
            double[] expected = {2453750.5, 0.8924826385462962963};

            double[] actual = ERFALib.ut12tai(ut11, ut12, dta);
            log.debug(String.format("ut1[%s, %s] -> tai[%s, %s]", ut11, ut12, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);
        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void ut12ttTest() {
        try {
            double ut11 = 2453750.5;
            double ut12 = 0.892104561;
            double dt = 64.8499;
            double[] expected = {2453750.5, 0.8928551385462962963};

            double[] actual = ERFALib.ut12tt(ut11, ut12, dt);
            log.debug(String.format("ut1[%s, %s] -> tt[%s, %s]", ut11, ut12, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void ut12utcTest() {
        try {
            double ut11 = 2453750.5;
            double ut12 = 0.892104561;
            double dut1 = 0.3341;
            double[] expected = {2453750.5, 0.8921006941018518519};

            double[] actual = ERFALib.ut12utc(ut11, ut12, dut1);
            log.debug(String.format("ut1[%s, %s] -> utc[%s, %s]", ut11, ut12, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);

            // UTC began at 1960 January 1 (JD 2436934.5), dates previous
            // should throw a DubiousYearException
            try {
                double dubious = 2436930.5;
                ERFALib.ut12utc(dubious, 0.0, dut1);
                Assert.fail("DubiousYearException expected");
            } catch (DubiousYearException e) {
                // expected
            }

            // dates before January 1 -4799 (JD -31738.5)
            // should throw an UnacceptableDateException
            try {
                double unacceptable = -31739.5;
                ERFALib.ut12utc(unacceptable, 0.0, dut1);
                Assert.fail("UnacceptableDateException expected");
            } catch (UnacceptableDateException e) {
                // expected
            }

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void utc2taiTest() {
        try {
            double utc1 = 2453750.5;
            double utc2 = 0.892100694;
            double[] expected = {2453750.5, 0.8924826384444444444};

            double[] actual = ERFALib.utc2tai(utc1, utc2);
            log.debug(String.format("utc[%s, %s] -> tai[%s, %s]", utc1, utc2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);

            // UTC began at 1960 January 1 (JD 2436934.5), dates previous
            // should throw a DubiousYearException
            try {
                double dubious = 2436930.5;
                ERFALib.tai2utc(dubious, 0.0);
                Assert.fail("DubiousYearException expected");
            } catch (DubiousYearException e) {
                // expected
            }

            // dates before January 1 -4799 (JD -31738.5)
            // should throw an UnacceptableDateException
            try {
                double unacceptable = -31739.5;
                ERFALib.tai2utc(unacceptable, 0.0);
                Assert.fail("UnacceptableDateException expected");
            } catch (UnacceptableDateException e) {
                // expected
            }

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

    @Test
    public void utc2ut1Test() {
        try {
            double utc1 = 2453750.5;
            double utc2 = 0.892100694;
            double dut1 = 0.3341;
            double[] expected = {2453750.5, 0.8921045608981481481};

            double[] actual = ERFALib.utc2ut1(utc1, utc2, dut1);
            log.debug(String.format("utc[%s, %s] -> ut1[%s, %s]", utc1, utc2, actual[0], actual[1]));

            Assert.assertEquals(expected[0], actual[0], 1e-6);
            Assert.assertEquals(expected[1], actual[1], 1e-12);

            // UTC began at 1960 January 1 (JD 2436934.5), dates previous
            // should throw a DubiousYearException
            try {
                double dubious = 2436930.5;
                ERFALib.utc2ut1(dubious, 0.0, dut1);
                Assert.fail("DubiousYearException expected");
            } catch (DubiousYearException e) {
                // expected
            }

            // dates before January 1 -4799 (JD -31738.5)
            // should throw an UnacceptableDateException
            try {
                double unacceptable = -31739.5;
                ERFALib.utc2ut1(unacceptable, 0.0, dut1);
                Assert.fail("UnacceptableDateException expected");
            } catch (UnacceptableDateException e) {
                // expected
            }

        } catch (Exception e) {
            log.error("unexpected exception", e);
            Assert.fail("unexpected exception: " + e);
        }
    }

}
