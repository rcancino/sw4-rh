import grails.test.AbstractCliTestCase

class CargaInicialTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCargaInicial() {

        execute(["carga-inicial"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
