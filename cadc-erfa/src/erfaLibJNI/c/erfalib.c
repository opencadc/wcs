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
#include "erfa.h"
#include "erfam.h"

/*
 * Throw a Java NoClassDefFoundError exception.
 */
jint throwNoClassDefError(JNIEnv *env, char *message) {
    jclass exClass;
    char *className = "java/lang/NoClassDefFoundError";

    exClass = (*env)->FindClass(env, className);
    if (exClass == NULL) {
        return throwNoClassDefError(env, className);
    }

    return (*env)->ThrowNew(env, exClass, message);
}

/*
 * Throw a Java ERFALibException exception.
 */
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
 * Throw a Java DubiousYearException exception.
 */
jint throwDubiousYearException(JNIEnv *env, char *message) {
    jclass exClass;
    char *className = "org/opencadc/erfa/DubiousYearException" ;

    exClass = (*env)->FindClass(env, className);
    if (exClass == NULL) {
        return throwNoClassDefError(env, className);
    }

    return (*env)->ThrowNew(env, exClass, message);
}

/*
 * Throw a Java UnacceptableDateException exception.
 */
jint throwUnacceptableDateException(JNIEnv *env, char *message) {
    jclass exClass;
    char *className = "org/opencadc/erfa/UnacceptableDateException" ;

    exClass = (*env)->FindClass(env, className);
    if (exClass == NULL) {
        return throwNoClassDefError(env, className);
    }

    return (*env)->ThrowNew(env, exClass, message);
}

/*
 *  - - - - - - - - -
 *   e r a T a i t t
 *  - - - - - - - - -
 *
 *  Time scale transformation:  International Atomic Time, TAI, to
 *  Terrestrial Time, TT.
 *
 *  Given:
 *     tai1,tai2  double    TAI as a 2-part Julian Date
 *
 *  Returned:
 *     tt1,tt2    double    TT as a 2-part Julian Date
 *
 *  Returned (function value):
 *                int       status:  0 = OK
 *
 *  Note:
 *
 *     tai1+tai2 is Julian Date, apportioned in any convenient way
 *     between the two arguments, for example where tai1 is the Julian
 *     Day Number and tai2 is the fraction of a day.  The returned
 *     tt1,tt2 follow suit.
 */
JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_eraTai2tt
    (JNIEnv *env, jclass thisObj, jdouble tai1, jdouble tai2) {
    double tt1, tt2;
    int status = eraTaitt(tai1, tai2, &tt1, &tt2);
    // this function is expected to always return 0, but...
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

/*
 *  - - - - - - - - -
 *   e r a T t t a i
 *  - - - - - - - - -
 *
 *  Time scale transformation:  Terrestrial Time, TT, to International
 *  Atomic Time, TAI.
 *
 *  Given:
 *     tt1,tt2    double    TT as a 2-part Julian Date
 *
 *  Returned:
 *     tai1,tai2  double    TAI as a 2-part Julian Date
 *
 *  Returned (function value):
 *                int       status:  0 = OK
 *
 *  Note:
 *
 *     tt1+tt2 is Julian Date, apportioned in any convenient way between
 *     the two arguments, for example where tt1 is the Julian Day Number
 *     and tt2 is the fraction of a day.  The returned tai1,tai2 follow
 *     suit.
 */
JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_eraTt2tai
    (JNIEnv *env, jclass thisObj, jdouble tt1, jdouble tt2) {
    double tai1, tai2;
    int status = eraTttai(tt1, tt2, &tai1, &tai2);
    // this function is expected to always return 0, but...
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
 *  - - - - - - - - - -
 *   e r a T a i u t c
 *  - - - - - - - - - -
 *
 *  Time scale transformation:  International Atomic Time, TAI, to
 *  Coordinated Universal Time, UTC.
 *
 *  Given:
 *     tai1,tai2  double   TAI as a 2-part Julian Date (Note 1)
 *
 *  Returned:
 *     utc1,utc2  double   UTC as a 2-part quasi Julian Date (Notes 1-3)
 *
 *  Returned (function value):
 *                int      status: +1 = dubious year (Note 4)
 *                                  0 = OK
 *                                 -1 = unacceptable date
 *
 *  Notes:
 *
 *  1) tai1+tai2 is Julian Date, apportioned in any convenient way
 *     between the two arguments, for example where tai1 is the Julian
 *     Day Number and tai2 is the fraction of a day.  The returned utc1
 *     and utc2 form an analogous pair, except that a special convention
 *     is used, to deal with the problem of leap seconds - see the next
 *     note.
 *
 *  2) JD cannot unambiguously represent UTC during a leap second unless
 *     special measures are taken.  The convention in the present
 *     function is that the JD day represents UTC days whether the
 *     length is 86399, 86400 or 86401 SI seconds.  In the 1960-1972 era
 *     there were smaller jumps (in either direction) each time the
 *     linear UTC(TAI) expression was changed, and these "mini-leaps"
 *     are also included in the ERFA convention.
 *
 *  3) The function eraD2dtf can be used to transform the UTC quasi-JD
 *     into calendar date and clock time, including UTC leap second
 *     handling.
 *
 *  4) The warning status "dubious year" flags UTCs that predate the
 *     introduction of the time scale or that are too far in the future
 *     to be trusted.  See eraDat for further details.
 *
 *  Called:
 *     eraUtctai    UTC to TAI
 */
JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_eraTai2utc
    (JNIEnv *env, jclass thisObj, jdouble tai1, jdouble tai2) {
    double utc1, utc2;
    int status = eraTaiutc(tai1, tai2, &utc1, &utc2);
    if (status != 0) {
        if (status == -1) {
            char *message = "year too early to compute Julian Date";
            throwUnacceptableDateException(env, message);
        } else if (status == 1) {
            char *message = "date either predates UTC or too far in the future";
            throwDubiousYearException(env, message);
        } else {
            char *message = "unknown error transforming TAI->UTC";
            throwERFALibException(env, message);
        }
    }
    double utc[2] = {utc1, utc2};
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    if (result == NULL) {
         char *message = "unable to allocate memory for TAI->UTC results array";
         throwERFALibException(env, message);
    }
    (*env)->SetDoubleArrayRegion(env, result, 0, 2, utc);
    return result;
}

/*
 *  - - - - - - - - - -
 *   e r a U t c t a i
 *  - - - - - - - - - -
 *
 *  Time scale transformation:  Coordinated Universal Time, UTC, to
 *  International Atomic Time, TAI.
 *
 *  Given:
 *     utc1,utc2  double   UTC as a 2-part quasi Julian Date (Notes 1-4)
 *
 *  Returned:
 *     tai1,tai2  double   TAI as a 2-part Julian Date (Note 5)
 *
 *  Returned (function value):
 *                int      status: +1 = dubious year (Note 3)
 *                                  0 = OK
 *                                 -1 = unacceptable date
 *
 *  Notes:
 *
 *  1) utc1+utc2 is quasi Julian Date (see Note 2), apportioned in any
 *     convenient way between the two arguments, for example where utc1
 *     is the Julian Day Number and utc2 is the fraction of a day.
 *
 *  2) JD cannot unambiguously represent UTC during a leap second unless
 *     special measures are taken.  The convention in the present
 *     function is that the JD day represents UTC days whether the
 *     length is 86399, 86400 or 86401 SI seconds.  In the 1960-1972 era
 *     there were smaller jumps (in either direction) each time the
 *     linear UTC(TAI) expression was changed, and these "mini-leaps"
 *     are also included in the ERFA convention.
 *
 *  3) The warning status "dubious year" flags UTCs that predate the
 *     introduction of the time scale or that are too far in the future
 *     to be trusted.  See eraDat for further details.
 *
 *  4) The function eraDtf2d converts from calendar date and time of day
 *     into 2-part Julian Date, and in the case of UTC implements the
 *     leap-second-ambiguity convention described above.
 *
 *  5) The returned TAI1,TAI2 are such that their sum is the TAI Julian
 *     Date.
 *
 *  Called:
 *     eraJd2cal    JD to Gregorian calendar
 *     eraDat       delta(AT) = TAI-UTC
 *     eraCal2jd    Gregorian calendar to JD
 */
JNIEXPORT jdoubleArray JNICALL Java_org_opencadc_erfa_ERFALib_eraUtc2tai
    (JNIEnv *env, jclass thisObj, jdouble utc1, jdouble utc2) {
    double tai1, tai2;
    int status = eraUtctai(utc1, utc2, &tai1, &tai2);
    if (status != 0) {
        if (status == -1) {
            char *message = "year too early to compute Julian Date";
            throwUnacceptableDateException(env, message);
        } else if (status == 1) {
            char *message = "date either predates UTC or too far in the future";
            throwDubiousYearException(env, message);
        } else {
            char *message = "unknown error transforming UTC->TAI";
            throwERFALibException(env, message);
        }
    }
    double tai[2] = {tai1, tai2};
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    if (result == NULL) {
         char *message = "unable to allocate memory for UTC->TAI results array";
         throwERFALibException(env, message);
    }
    (*env)->SetDoubleArrayRegion(env, result, 0, 2, tai);
    return result;
}
