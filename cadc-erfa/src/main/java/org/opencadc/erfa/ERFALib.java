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
     * International Atomic Time, TAI, to Terrestrial Time, TT.
     * tai[0,1] is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tai1 is the Julian
     * Day Number and tai2 is the fraction of a day.  The returned tt1,tt2
     * follow suit.
     *
     * @param tai as a two part Julian Date
     * @return tt as a two part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] tai2tt(double[] tai) throws ERFALibException {
        if (tai.length != 2) {
            throw new IllegalArgumentException("expected 2 elements in double[] tai");
        }
        return tai2tt(tai[0], tai[1]);
    }

    /**
     * Terrestrial Time, TT, to International Atomic Time, TAI.
     * tt[0,1] is a two part Julian Date, apportioned in any convenient
     * way between the two arguments, for example where tt1 is the Julian
     * Day Number and tt2 is the fraction of a day.  The returned tai1,tai2
     * follow suit.
     *
     * @param tt as a two part Julian Date
     * @return tai as a two part Julian Date.
     * @throws ERFALibException if an error occurs doing the conversion.
     **/
    public static double[] tt2tai(double[] tt) throws ERFALibException {
        if (tt.length != 2) {
            throw new IllegalArgumentException("expected 2 elements in double[] tt");
        }
        return tt2tai(tt[0], tt[1]);
    }

    private static native double[] tai2tt(double tia1, double tai2) throws ERFALibException;
    private static native double[] tt2tai(double tt1, double tt2) throws ERFALibException;

}
