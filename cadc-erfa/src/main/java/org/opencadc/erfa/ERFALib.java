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

import org.apache.log4j.Logger;

/** <p>ERFALib is a Java JNI wrapper around Time transformations in the
 * ERFA (Essential Routines for Fundamental Astronomy) C library. Specifically:
 * </p>
 * <code>
 * <li>TIA to TT</li>
 * <li>TAI to UT1</li>
 * <li>TAI to UTC</li>
 * <li>TT to TAI</li>
 * <li>TT to UT1</li>
 * <li>UT1 to TAI</li>
 * <li>UT1 to TT</li>
 * <li>UT1 to UTC</li>
 * <li>UTC to TAI</li>
 * <li>UTC to UT1</li>
 * </code>
 * <p>where
 * </p>
 * <code>
 * <li>TAI - International Atomic Time</li>
 * <li>TT - Terrestrial Time</li>
 * <li>UT1 - Universal Time</li>
 * <li>UTC - Coordinated Universal Time</li>
 * </code>
 */
public class ERFALib {
    private static final Logger log = Logger.getLogger(ERFALib.class);

    static {
        try {
            NativeUtil.loadJNI(ERFALib.class.getClassLoader(), "liberfaLibJNI");
        } catch (NativeInitializationException e) {
            log.info("Checking library file liberfaLibJNI: FAIL");
        }
    }

    public ERFALib() {
    }

    /**
     * <p>Time scale transformation: International Atomic Time, TAI, to Terrestrial Time, TT.
     * </p>
     * <p>tai1+tai2 is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tai1 is the Julian
     * Day Number and tai2 is the fraction of a day.  The returned tt1,tt2
     * follow suit.
     * </p>
     * @param tai1,tai2 TAI as a 2-part Julian Date
     * @return TT as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] tai2tt(double tai1, double tai2)
        throws ERFALibException {
        return eraTai2tt(tai1, tai2);
    }

    /**
     * <p>Time scale transformation: International Atomic Time, TAI, to Universal Time, UT1.
     * </p>
     * <p>tai1+tai2 is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tai1 is the Julian
     * Day Number and tai2 is the fraction of a day.  The returned ut1+ut2
     * follow suit.
     * The argument dta, i.e. UT1-TAI, is an observed quantity, and is
     * available from IERS tabulations.
     * </p>
     * @param tai1,tai2 TAI as a 2-part Julian Date
     * @param dta UT1-TAI in seconds
     * @return UT1 as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] tai2ut1(double tai1, double tai2, double dta)
        throws ERFALibException {
        return eraTai2ut1(tai1, tai2, dta);
    }

    /**
     * <p>Time scale transformation: International Atomic Time, TAI, to Coordinated Universal Time, UTC.
     * </p>
     * <p>tai1+tai2 is a 2-part Julian Date, apportioned in any convenient way
     * between the two arguments, for example where tai1 is the Julian
     * Day Number and tai2 is the fraction of a day.  The returned
     * utc1+utc2 form an analogous pair, except that a special convention
     * is used, to deal with the problem of leap seconds.
     * </p>
     * <p>JD cannot unambiguously represent UTC during a leap second unless
     * special measures are taken.  The convention in the present
     * function is that the JD day represents UTC days whether the
     * length is 86399, 86400 or 86401 SI seconds.  In the 1960-1972 era
     * there were smaller jumps (in either direction) each time the
     * linear UTC(TAI) expression was changed, and these "mini-leaps"
     * are also included in the ERFA convention.
     * </p>
     * @param tai1,tai2 TAI as a 2-part Julian Date.
     * @return UTC as a 2-part quasi Julian Date.
     * @throws ERFALibException if an error occurs doing the transformation.
     * @throws DubiousYearException if a date predates the introduction of the UTC timescale
     *                              or are too far in the future to be trusted.
     * @throws UnacceptableDateException if a date is so early that a Julian Date
     *                                   could not be computed.
     **/
    public static double[] tai2utc(double tai1, double tai2)
        throws ERFALibException, DubiousYearException, UnacceptableDateException {
        return eraTai2utc(tai1, tai2);
    }

    /**
     * <p>Time scale transformation: Terrestrial Time, TT, to International Atomic Time, TAI.
     * </p>
     * <p>tt1+tt2 is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tt1 is the Julian
     * Day Number and tt2 is the fraction of a day.  The returned tai1+tai2
     * follow suit.
     * </p>
     * @param tt1,tt2 TT as a 2-part Julian Date
     * @return TAI as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] tt2tai(double tt1, double tt2)
        throws ERFALibException {
        return eraTt2tai(tt1, tt2);
    }

    /**
     * <p>Time scale transformation: Terrestrial Time, TT, to Universal Time, UT1.
     * </p>
     * <p>tt1+tt2 is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tt1 is the Julian
     * Day Number and tt2 is the fraction of a day.  The returned ut11+ut12
     * follow suit.
     * </p>
     * <p>The argument dt is classical Delta T.
     * </p>
     * @param tt1,tt2 TT as a 2-part Julian Date.
     * @param dt TT-UT1 in seconds.
     * @return UT1 as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the transformation.
     **/
    public static double[] tt2ut1(double tt1, double tt2, double dt)
        throws ERFALibException {
        return eraTt2ut1(tt1, tt2, dt);
    }

    /**
     * <p>Time scale transformation: Universal Time, UT1, to International Atomic Time, TAI.
     * </p>
     * <p>ut11+ut12 is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where ut11 is the Julian
     * Day Number and ut12 is the fraction of a day.  The returned tai1+tai2
     * follow suit.
     * </p>
     * <p>The argument dta, i.e. UT1-TAI, is an observed quantity, and is
     * available from IERS tabulations.
     * </p>
     * @param ut11,ut12 UT1 as a 2-part Julian Date
     * @param dta UT1-TAI in seconds
     * @return TAI as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] ut12tai(double ut11, double ut12, double dta)
        throws ERFALibException {
        return eraUt12tai(ut11, ut12, dta);
    }

    /**
     * <p>Time scale transformation: Universal Time, UT1, to Terrestrial Time, TT.
     * </p>
     * <p> ut11+ut12 is Julian Date, apportioned in any convenient way
     * between the two arguments, for example where ut11 is the Julian
     * Day Number and ut12 is the fraction of a day.  The returned
     * tt1+tt2 follow suit.
     * </p>
     * <p>The argument dt is classical Delta T.
     * </p>
     * @param ut11,ut12 UT1 as a 2-part Julian Date.
     * @param dt TT-UT1 in seconds.
     * @return TT as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the transformation.
     */
    public static double[] ut12tt(double ut11, double ut12, double dt)
        throws ERFALibException {
        return eraUt12tt(ut11, ut12, dt);
    }

    /**
     * <p>Time scale transformation: Universal Time, UT1, to Coordinated Universal Time, UTC.
     * </p>
     * <p>ut11+ut12 is Julian Date, apportioned in any convenient way
     * between the two arguments, for example where ut11 is the Julian
     * Day Number and ut12 is the fraction of a day.  The returned utc1+utc2
     * form an analogous pair, except that a special convention
     * is used, to deal with the problem of leap seconds.
     * </p>
     * <p>JD cannot unambiguously represent UTC during a leap second unless
     * special measures are taken.  The convention in the present
     * function is that the returned quasi-JD UTC1+UTC2 represents UTC
     * days whether the length is 86399, 86400 or 86401 SI seconds.
     * </p>
     * <p>Delta UT1 can be obtained from tabulations provided by the
     * International Earth Rotation and Reference Systems Service.  The
     * value changes abruptly by 1s at a leap second;  however, close to
     * a leap second the algorithm used here is tolerant of the "wrong"
     * choice of value being made.
     * </p>
     * @param ut11,ut12 UT1 as a 2-part Julian Date
     * @param dut1 Delta UT1: UT1-UTC in seconds
     * @return UTC as a 2-part quasi Julian Date
     * @throws ERFALibException if an error occurs doing the transformation.
     * @throws DubiousYearException if a date predates the introduction of the UTC timescale
     *                              or are too far in the future to be trusted.
     * @throws UnacceptableDateException if a date is so early that a Julian Date
     *                                   could not be computed.
     */
    public static double[] ut12utc(double ut11, double ut12, double dut1)
        throws ERFALibException, DubiousYearException, UnacceptableDateException {
        return eraUt12utc(ut11, ut12, dut1);
    }

    /**
     * <p>Time scale transformation: Coordinated Universal Time, UTC, to International Atomic Time, TAI.
     * </p>
     * <p>utc1+utc2 is a 2-part Julian Date, apportioned in any convenient way
     * between the two arguments, for example where tai1 is the Julian
     * Day Number and tai2 is the fraction of a day.  The returned
     * utc1+utc2 form an analogous pair, except that a special convention
     * is used, to deal with the problem of leap seconds.
     * </p>
     * <p>JD cannot unambiguously represent UTC during a leap second unless
     * special measures are taken.  The convention in the present
     * function is that the JD day represents UTC days whether the
     * length is 86399, 86400 or 86401 SI seconds.  In the 1960-1972 era
     * there were smaller jumps (in either direction) each time the
     * linear UTC(TAI) expression was changed, and these "mini-leaps"
     * are also included in the ERFA convention.
     * </p>
     * @param utc1,utc2 UTC as a 2-part quasi Julian Date.
     * @return TAI as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the transformation.
     * @throws DubiousYearException if a date predates the introduction of the UTC timescale
     *                              or are too far in the future to be trusted.
     * @throws UnacceptableDateException if a date is so early that a Julian Date
     *                                   could not be computed.
     **/
    public static double[] utc2tai(double utc1, double utc2)
        throws ERFALibException, DubiousYearException, UnacceptableDateException {
        return eraUtc2tai(utc1, utc2);
    }

    /**
     * <p>Time scale transformation: Coordinated Universal Time, UTC, to Universal Time, UT1.
     * </p>
     * <p>utc1+utc2 is a 2-part Julian Date, apportioned in any convenient way
     * between the two arguments, for example where utc1 is the Julian
     * Day Number and utc2 is the fraction of a day.  The returned
     * ut11+ut12 form an analogous pair, except that a special convention
     * is used, to deal with the problem of leap seconds.
     * </p>
     * <p>JD cannot unambiguously represent UTC during a leap second unless
     * special measures are taken.  The convention in the present
     * function is that the JD day represents UTC days whether the
     * length is 86399, 86400 or 86401 SI seconds.  In the 1960-1972 era
     * there were smaller jumps (in either direction) each time the
     * linear UTC(TAI) expression was changed, and these "mini-leaps"
     * are also included in the ERFA convention.
     * </p>
     * @param utc1,utc2 UTC as a 2-part quasi Julian Date.
     * @return UT1 as a 2-part Julian Date.
     * @throws ERFALibException if an error occurs doing the transformation.
     * @throws DubiousYearException if a date predates the introduction of the UTC timescale
     *                              or are too far in the future to be trusted.
     * @throws UnacceptableDateException if a date is so early that a Julian Date
     *                                   could not be computed.
     **/
    public static double[] utc2ut1(double utc1, double utc2, double dut1)
        throws ERFALibException, DubiousYearException, UnacceptableDateException {
        return eraUtc2ut1(utc1, utc2, dut1);
    }

    /**
     * native methods
     */
    private static native double[] eraTai2tt(double tia1, double tai2)
        throws ERFALibException;

    private static native double[] eraTai2ut1(double tia1, double tai2, double dta)
        throws ERFALibException;

    private static native double[] eraTai2utc(double tai1, double tai2)
        throws ERFALibException, DubiousYearException, UnacceptableDateException;

    private static native double[] eraTt2tai(double tt1, double tt2)
        throws ERFALibException;

    private static native double[] eraTt2ut1(double tt1, double tt2, double dt)
        throws ERFALibException;

    private static native double[] eraUt12tai(double ut1, double ut2, double dta)
        throws ERFALibException;

    private static native double[] eraUt12tt(double ut11, double ut12, double dt)
        throws ERFALibException;

    private static native double[] eraUt12utc(double ut1, double ut2, double dut1)
        throws ERFALibException;

    private static native double[] eraUtc2tai(double utc1, double utc2)
        throws ERFALibException, DubiousYearException, UnacceptableDateException;

    private static native double[] eraUtc2ut1(double utc1, double utc2, double dut1)
        throws ERFALibException, DubiousYearException, UnacceptableDateException;

}
