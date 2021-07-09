package ca.nrc.cadc.wcs;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import ca.nrc.cadc.util.Log4jInit;
import ca.nrc.cadc.wcs.exceptions.WCSLibRuntimeException;

public class SpectralUtilTest {

    private static final Logger log = Logger.getLogger(SpectralUtilTest.class);

    static
    {
        Log4jInit.setLevel("ca.nrc.cadc.wcs", Level.INFO);
    }

    @Test
    public void testEmptyParameters() {
        try {
            try {
                String[] ctypes = {};
                String[] cunits = {};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Empty arrays should not throw an exception");
            }

            try {
                String[] ctypes = {};
                String[] cunits = {"Hz"};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Empty ctype array should not throw an exception");
            }

            try {
                String[] ctypes = {""};
                String[] cunits = {"Hz"};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Empty ctype array should not throw an exception");
            }

            try {
                String[] ctypes = {"FREQ-F2W"};
                String[] cunits = {};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Empty cunit array should not throw an exception");
            }

            try {
                String[] ctypes = {"FREQ-F2W"};
                String[] cunits = {""};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Empty cunit array should not throw an exception");
            }
        } catch(Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }

    @Test
    public void testValidCtypesCunits() {
        try {
            String[] ctypes = {"FREQ", "ENER", "WAVN", "VRAD", "WAVE", "VOPT", "ZOPT", "AWAV", "VELO", "BETA", "FREQ", "ENER", "WAVN", "VRAD", "WAVE", "VOPT"};
            String[] cunits = {"Hz", "kHz", "MHz", "GHz", "eV", "keV", "MeV", "GeV", "m", "cm", "mm", "um", "µm", "nm", "Angstrom", "A"};
            SpectralUtil.isValidCunit(ctypes, cunits);
        } catch(Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }
    
    @Test
    public void testInvalidCtypes() {
        try {
            try {
                String[] ctypes = {"WAVE", "FOO", "FREQ"};
                String[] cunits = {"m", "Hz", "eV"};
                SpectralUtil.isValidCunit(ctypes, cunits);
            } catch (WCSLibRuntimeException e) {
                Assert.fail("Unknown CTYPE's should not throw an exception");
            }
        } catch(Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }

    @Test
    public void testInvalidCunits() {
        try {
            try {
                String[] ctypes = {"WAVE", "ENER", "FREQ"};
                String[] cunits = {"m", "BAR", "eV"};
                SpectralUtil.isValidCunit(ctypes, cunits);
                Assert.fail("Unknown CUNIT should throw an exception");
            } catch (WCSLibRuntimeException expected) { }
        } catch(Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }
    
}
