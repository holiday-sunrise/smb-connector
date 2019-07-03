package org.mule.modules.smb.automation.functional;

import static org.junit.Assert.assertTrue;
import static org.mule.modules.smb.automation.functional.TestDataBuilder.DIR_DELETE_TEST_NAME;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.smb.SmbConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DirectoryDeleteTest extends AbstractTestCase<SmbConnector> {

    public DirectoryDeleteTest() {
        super(SmbConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().directoryCreate(DIR_DELETE_TEST_NAME);
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void verify() {
        try {
            assertTrue(getConnector().directoryDelete(DIR_DELETE_TEST_NAME, true));
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}