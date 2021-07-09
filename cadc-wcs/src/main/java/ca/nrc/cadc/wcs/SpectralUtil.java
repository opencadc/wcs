/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2021.                            (c) 2021.
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
************************************************************************
*/

package ca.nrc.cadc.wcs;

import java.util.Arrays;
import java.util.List;
import ca.nrc.cadc.wcs.exceptions.WCSLibRuntimeException;

public class SpectralUtil
{
    final static List<String> SPECTRAL_CTYPES = Arrays.asList(
        "FREQ", "ENER", "WAVN", "VRAD", "WAVE", "VOPT", "ZOPT", 
        "AWAV", "VELO", "BETA" 
    );

    final static List<String> SPECTRAL_CUNITS = Arrays.asList(
        "Hz", "kHz", "MHz", "GHz", "eV", "keV", "MeV", "GeV", 
        "m", "cm", "mm", "um", "µm", "nm", "Angstrom", "A"
    );

    /**
     * Method to check if known spectral CTYPE's have an unknown 
     * spectral CUNIT.
     * Passing an invalid spectral CUNIT to WCSLib version 6.4 
     * causes a segmentation fault.
     * This method looks for known spectral CTYPE's, and checks 
     * the corresponding CUNIT. If the CUNIT is invalid, 
     * it will throw a WCSLibRuntimeException.
     * @param ctypes array of CTYPE's
     * @param cunits array of CUNIT's
     * @throws WCSLibRuntimeException for an unknown spectral CUNIT
     */
    public static void isValidCunit(final String[] ctypes, 
                                    final String[] cunits) 
        throws WCSLibRuntimeException {

        if (ctypes.length == 0 || cunits.length == 0) {
            return;
        }

        for (int i = 0; i < ctypes.length; i++) {
            String ctype = ctypes[i];
            for (String spectralCtype: SPECTRAL_CTYPES) {
                if (spectralCtype.equalsIgnoreCase(ctype)) {
                    String cunit = cunits[i];
                    if (cunit.isEmpty()) {
                        continue;
                    }
                    boolean found = false;
                    for (String spectralCunit : SPECTRAL_CUNITS) {
                        if (spectralCunit.equalsIgnoreCase(cunit)) {
                            found = true;
                            continue;
                        }
                    }
                    if (!found) {
                        throw new WCSLibRuntimeException(
                            "Invalid coordinate transformation parameters", 6);
                    }
                }
            }
        }
    }

}
