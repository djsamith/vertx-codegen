package io.vertx.test.codegen;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Utils {

  static <E> HashSet<E> set(E... elements) {
    HashSet<E> set = new HashSet<>();
    Collections.addAll(set, elements);
    return set;
  }

  static void assertProcess(BiConsumer<ProcessingEnvironment, RoundEnvironment> test) {
    File f = null;
    try {
      f = Files.createTempFile("test", ".java").toFile();
      try (PrintWriter writer = new PrintWriter(f)) {
        writer.print("class Empty {}");
      }
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), Charset.defaultCharset());
    JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(System.out),
        fileManager,
        diagnostics,
        Collections.singletonList("-proc:only"),
        Collections.emptyList(),
        fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(f)));
    task.setLocale(Locale.getDefault());
    task.setProcessors(Collections.singleton(new AbstractProcessor() {
      @Override
      public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("*");
      }
      @Override
      public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        test.accept(processingEnv, roundEnv);
        return true;
      }
    }));
    assertTrue(task.call());
  }
}
