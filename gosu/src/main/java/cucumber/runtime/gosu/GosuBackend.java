package cucumber.runtime.gosu;

import cucumber.runtime.Backend;
import cucumber.runtime.Glue;
import cucumber.runtime.UnreportedStepExecutor;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.snippets.FunctionNameGenerator;
import cucumber.runtime.snippets.SnippetGenerator;
import gherkin.formatter.model.Step;
import gw.lang.Gosu;
import gw.lang.function.AbstractBlock;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.gs.IProgramInstance;

import java.util.List;
import java.util.regex.Pattern;

public class GosuBackend implements Backend {
    public static GosuBackend instance;

    private final ResourceLoader resourceLoader;
    private final SnippetGenerator snippetGenerator = new SnippetGenerator(new GosuSnippet());
    private Glue glue;

    public GosuBackend(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        instance = this;
    }

    @Override
    public void loadGlue(Glue glue, List<String> gluePaths) {
        this.glue = glue;
//        GlueSource source = new GlueSource();
//        for (String gluePath : gluePaths) {
//            for (Resource glueScript : resourceLoader.resources(gluePath, ".gsp")) {
//                this.runGlueScript(glueScript);
//            }
//        }
        this.runGlueScript(null);
    }

    private void runGlueScript(Resource glueScript) {
//        Class clazz = ReflectUtil.getClass(glueScript.getClassName(".gsp")).getBackingClass();
        Gosu.init();
        Class clazz = ReflectUtil.getClass("cucumber.features.MyStepDefinitions").getBackingClass();
        try {
            ((IProgramInstance) (clazz.newInstance())).evaluate(null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUnreportedStepExecutor(UnreportedStepExecutor executor) {

    }

    @Override
    public void buildWorld() {

    }

    @Override
    public void disposeWorld() {

    }

    @Override
    public String getSnippet(Step step, FunctionNameGenerator functionNameGenerator) {
        return snippetGenerator.getSnippet(step, null);
    }

    public void addStepDefinition(String regexp, Object body) {
        AbstractBlock block = (AbstractBlock) body;
        glue.addStepDefinition(new GosuStepDefinition(Pattern.compile(regexp), block, currentLocation()));
    }

    private static StackTraceElement currentLocation() {
        Throwable t = new Throwable();
        StackTraceElement[] stackTraceElements = t.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (isGosuFile(stackTraceElement.getFileName())) {
                return stackTraceElement;
            }
        }
        throw new RuntimeException("Couldn't find location for step definition");
    }

    private static boolean isGosuFile(String fileName) {
        return fileName != null && fileName.endsWith(".gsp");
    }
}
