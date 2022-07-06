package org.asciidoc.intellij.inspections;

import com.intellij.grazie.ide.inspection.grammar.GrazieInspection;
import com.intellij.ide.plugins.PluginEnabler;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.intellij.testFramework.PlatformTestUtil;
import org.asciidoc.intellij.quickfix.AsciiDocChangeCaseForAnchor;
import org.junit.Ignore;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Schwartz
 * This started to fail after adding Grazie Professional in version 0.3.105, see
 * <a href="https://youtrack.jetbrains.com/issue/IDEA-205964">IDEA-205964</a> and
 * <a href="https://youtrack.jetbrains.com/issue/GRZ-504/">GRZ-504</a>.
 * Will keep disabled until the IDEA issue has been resolved.
 */
@Ignore
public class AsciiDocGrammarInspectionTest extends AsciiDocQuickFixTestBase {

  static {
    Set<PluginId> disabled = new HashSet<>();
    Set<PluginId> enabled = new HashSet<>();

    // to avoid:  java.lang.NoClassDefFoundError: Could not initialize class ai.grazie.nlp.tokenizer.spacy.SpacyBaseLanguage
    disabled.add(PluginId.getId("com.intellij.grazie.pro"));
    enabled.add(PluginId.getId("tanvd.grazi"));

    // to improve performance, remove plugins used for debugging in interactive mode
    disabled.add(PluginId.getId("PsiViewer"));
    disabled.add(PluginId.getId("PlantUML integration"));
    disabled.add(PluginId.getId("com.intellij.platform.images"));
    disabled.add(PluginId.getId("com.intellij.javafx"));

    PluginEnabler.HEADLESS.disableById(disabled);
    PluginEnabler.HEADLESS.enableById(enabled);
  }

  private static final String NAME = new AsciiDocChangeCaseForAnchor().getName();

  @Override
  public void setUp() throws Exception {
    super.setUp();
    //noinspection unchecked
    myFixture.enableInspections(GrazieInspection.class, SpellCheckingInspection.class);
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
  }

  public void testAttributeParsing() {
    doTestNoFix(NAME, true);
  }

  public void testSomethingQuoted() {
    doTestNoFix(NAME, true);
  }

  public void testSectionWithAttributes() {
    doTestNoFix(NAME, true);
  }

  public void testSeparateBehavior() {
    doTestNoFix(NAME, true);
  }

  @Override
  protected String getBasePath() {
    return "inspections/grammar";
  }
}
