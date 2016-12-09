package cucumber.api.gosu.en;

import cucumber.runtime.gosu.GosuBackend;
import gw.lang.function.Function0;

/**
 * Created by samarak on 9/12/2016.
 */
public class DSL {
    private static GosuBackend backend = GosuBackend.instance;

    public static void Given(String regexp, Function0 fn) {
        backend.addStepDefinition(regexp, fn);
    }

    public static void When(String regexp, Function0 fn) {
        backend.addStepDefinition(regexp, fn);
    }

    public static void Then(String regexp, Function0 fn) {
        backend.addStepDefinition(regexp, fn);
    }
}
