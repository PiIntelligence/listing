package com.github.sormuras.listing;

import static com.github.sormuras.listing.Listable.IDENTITY;
import static com.github.sormuras.listing.Listable.NEWLINE;
import static com.github.sormuras.listing.Listable.SPACE;
import static com.github.sormuras.listing.Text.assertEquals;
import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static java.util.Locale.GERMAN;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class ListingTest {

  @Test
  public void addChar() {
    assertEquals("\0", new Listing().add('\0').toString());
    assertEquals("'0'", new Listing().add(Listable.of('0')).toString());
    assertEquals("'\\u0000'", new Listing().add(Listable.of('\0')).toString());
  }

  @Test
  public void addFormattedString() {
    assertEquals("abc", new Listing().add("a%sc", "b").toString());
    assertEquals("abc", new Listing().add("abc", new Object[0]).toString());
    assertEquals("3,14159", new Listing().add(GERMAN, "%.5f", PI).toString());
    assertEquals("3,14159", new Listing().add(GERMAN, "3,14159").toString());
  }

  @Test
  public void addJavaName() {
    assertEquals("java.lang.Object", new Listing().add(JavaName.of(Object.class)).toString());
    assertEquals("byte[][]", new Listing().add(JavaName.of(byte[][].class)).toString());
  }

  @Test
  public void addListable() {
    assertEquals("", new Listing().add(IDENTITY).toString());
    assertEquals("α\n", new Listing().add('α').add(NEWLINE).toString());
    assertEquals(" ", new Listing().add(SPACE).toString());
  }

  @Test
  public void addListOfListables() {
    List<Listable> list = new ArrayList<>();
    assertEquals("", new Listing().add(list).toString());
    list.add(Listable.of('a'));
    assertEquals("'a'", new Listing().add(list).toString());
    list.add(Listable.of('z'));
    assertEquals("'a'\n'z'", new Listing().add(list).toString());
    assertEquals("'a'-'z'", new Listing().add(list, "-").toString());
  }

  @Test
  public void beginAndEnd() {
    Listing listing = new Listing();
    listing.add("BEGIN").newline();
    listing.add("END.").newline();
    assertEquals("BEGIN\nEND.\n", listing.toString());
    assertEquals(asList("BEGIN", "END."), listing.getCollectedLines());
  }

  @Test
  public void defaults() {
    Listing listing = new Listing();
    assertEquals(0, listing.getIndentationDepth());
    assertEquals("  ", listing.getIndentationString());
    assertEquals("\n", listing.getLineSeparator());
  }

  @Test
  public void indent() {
    Listing listing = new Listing();
    listing.add("BEGIN").newline();
    listing.indent(1).add("writeln('Hello, world.')").newline().indent(-1);
    listing.add("END.").newline();
    assertEquals(0, listing.getIndentationDepth());
    assertEquals(getClass(), "indent", listing);
    listing.indent(-100);
    assertEquals(0, listing.getIndentationDepth());
  }

  @Test
  public void newlineProducesOnlyOneSingleBlankLine() {
    Listing listing = new Listing();
    assertEquals(0, listing.getCurrentLine().length());
    listing.newline().newline().newline().newline().newline();
    assertEquals(0, listing.getCurrentLine().length());
    listing.add("BEGIN").newline().newline().newline().newline().newline();
    assertEquals(0, listing.getCurrentLine().length());
    listing.add("END.").newline().newline().newline().newline().newline();
    assertEquals(0, listing.getCurrentLine().length());
    assertEquals("BEGIN\n\nEND.\n\n", listing.toString());
    assertEquals(0, listing.getCurrentLine().length());
    assertEquals(asList("BEGIN", "", "END.", ""), listing.getCollectedLines());
  }

  @Test
  public void script() throws Exception {
    Listing listing = new Listing();
    listing.add("var fun1 = function(name) { return \"Hi \" + name; };").newline();
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    engine.eval(listing.toString());
    Invocable invocable = (Invocable) engine;
    assertEquals("Hi Bob", invocable.invokeFunction("fun1", "Bob"));
  }
}
