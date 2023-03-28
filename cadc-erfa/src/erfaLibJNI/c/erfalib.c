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
 ************************************************************************
 */

#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
//#include "liberfa/erfa.h"
//#include "liberfa/erfam.h"
#include "erfa.h"
#include "erfam.h"


jint throwNoClassDefError( JNIEnv *env, char *message ){
    jclass exClass;
    char *className = "java/lang/NoClassDefFoundError";

    exClass = (*env)->FindClass(env, className);
    if (exClass == NULL) {
        return throwNoClassDefError(env, className);
    }

    return (*env)->ThrowNew(env, exClass, message);
}


jint throwERFALibException(JNIEnv *env, char *message) {
    jclass exClass;
    char *className = "org/opencadc/erfa/ERFALibException" ;

    exClass = (*env)->FindClass(env, className);
    if (exClass == NULL) {
        return throwNoClassDefError(env, className);
    }

    return (*env)->ThrowNew(env, exClass, message);
}

 /*
  **  - - - - - - - - -
  **   e r a T t t a i
  **  - - - - - - - - -
  **
  **  Time scale transformation:  Terrestrial Time, TT, to International
  **  Atomic Time, TAI.
  **
  **  Given:
  **     tt1,tt2    double    TT as a 2-part Julian Date
  **
  **  Returned:
  **     tai1,tai2  double    TAI as a 2-part Julian Date
  **
  **  Returned (function value):
  **                int       status:  0 = OK
  **
  **  Note:
  **
  **     tt1+tt2 is Julian Date, apportioned in any convenient way between
  **     the two arguments, for example where tt1 is the Julian Day Number
  **     and tt2 is the fraction of a day.  The returned tai1,tai2 follow
  **     suit.
  */
  JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_tt2tai
    (JNIEnv *env, jclass thisObj, jdouble tt1, jdouble tt2) {
    double tai1, tai2;
    int status = eraTttai(tt1, tt2, &tai1, &tai2);
    // this function should always return 0, but...
    if (status != 0) {
        char *message = "unknown error converting TAI->TT";
        throwERFALibException(env, message);
    }
    double tai[2] = {tai1, tai2};
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    if (result == NULL) {
         char *message = "unable to allocate memory for TT->TAI results array";
         throwERFALibException(env, message);
    }
    (*env)->SetDoubleArrayRegion(env, result, 0, 2, tai);
    return result;
}

/*
**  - - - - - - - - -
**   e r a T a i t t
**  - - - - - - - - -
**
**  Time scale transformation:  International Atomic Time, TAI, to
**  Terrestrial Time, TT.
**
**  Given:
**     tai1,tai2  double    TAI as a 2-part Julian Date
**
**  Returned:
**     tt1,tt2    double    TT as a 2-part Julian Date
**
**  Returned (function value):
**                int       status:  0 = OK
**
**  Note:
**
**     tai1+tai2 is Julian Date, apportioned in any convenient way
**     between the two arguments, for example where tai1 is the Julian
**     Day Number and tai2 is the fraction of a day.  The returned
**     tt1,tt2 follow suit.
*/
JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_tai2tt
    (JNIEnv *env, jclass thisObj, jdouble tai1, jdouble tai2) {
    double tt1, tt2;
    int status = eraTaitt(tai1, tai2, &tt1, &tt2);
    // this function should always return 0, but...
    if (status != 0) {
        char *message = "unknown error converting TAI->TT";
        throwERFALibException(env, message);
    }
    double tt[2] = {tt1, tt2};
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    if (result == NULL) {
         char *message = "unable to allocate memory for TAI->TT results array";
         throwERFALibException(env, message);
    }
    (*env)->SetDoubleArrayRegion(env, result, 0, 2, tt);
    return result;
}
