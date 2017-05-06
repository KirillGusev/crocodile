package afs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ovchi on 03.05.2017.
 */
public class ConsoleTest {
    Console my;
    @Before
    public void setUp() throws Exception {
        my=new Console();
    }

    @Test
    public void splitTest() throws Exception {
        assertArrayEquals(my.split("cd \"Мои документы\" Music"),new String[]{"cd","Мои документы","Music"});
        assertArrayEquals(my.split("cd \"Мои документы"),new String[]{"cd","Мои документы"});
        assertArrayEquals(my.split("cd \"Мои документы\"\""),new String[]{"cd","Мои документы"});
        assertArrayEquals(my.split("cd \"Мои документы\""+" "),new String[]{"cd","Мои документы"});
        assertArrayEquals(my.split("cd \"Мои документы\"/\"Мои документы\""),new String[]{"cd","Мои документы/Мои документы"});
        assertArrayEquals(my.split("cd \"Мои документы\""+"         "),new String[]{"cd","Мои документы"});
        assertArrayEquals(my.split("cd \"Мои документы\"/\\ Music"),new String[]{"cd","Мои документы/\\","Music"});
        assertArrayEquals(my.split("cd \"Мои документы\"/\\ Music"),new String[]{"cd","Мои документы/\\","Music"});
    }
}