package com.tayek.util.core;
import static org.junit.Assert.*;
import org.junit.Test;
public class TextsTestCase {
    @Test public void testUnQuoteUndoesQuote() {
        String original="a\tb\r\nc\\d";
        String quoted=Texts.quote(original,"\t\r\n\\");
        assertEquals("a\\tb\\r\\nc\\\\d",quoted);
        assertEquals(original,Texts.unQuote(quoted));
    }
    @Test public void testUnQuoteOtherEscapedCharacter() {
        assertEquals("x&y",Texts.unQuote(Texts.quote("x&y","&")));
    }
    @Test public void testUnQuoteTrailingBackslashIsKept() {
        assertEquals("abc\\",Texts.unQuote("abc\\"));
    }
    @Test public void testUnQuotePlainStringUnchanged() {
        assertEquals("plain",Texts.unQuote("plain"));
    }
}
