CADC WCS 2.0
============

The cadc-wcs library is the java interface to `wcslib` (http://www.atnf.csiro.au/people/mcalabre/WCS/). 

This library can be built on its own but testing and beyond requires wcslib. The JNI binding library is
compiled and included inside the `cadc-wcs` JAR file and deployed during class loading.

Versions: 
- cadc-wcs-1.x binaries on JCenter are linked against wcslib-4
- cadc-wcs-2.x binaries on JCenter are linked against >= wcslib-5

Building & Testing
------------------

Gradle is used to build the Java, generate the JNI headers, compile the C, and link the Shared Object.  Due to
the complexity of these moving pieces, they need to be run in a certain order:

 1. `>$ gradle -i -x test clean build` -- build the Java and compile the C.  Omit the tests as they're not ready yet.
 1. `>$ gradle -i assembleSharedJar` -- Copy the Shared Object into the build folder and build the JAR file.
 1. `>$ gradle -i test` -- Now run the tests as the JAR file is complete
