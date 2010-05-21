package org.openmole.ui.console;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.openmole.misc.workspace.ForbidenPasswordProvider;
import org.openmole.ui.console.internal.Activator;
import org.openmole.ui.console.internal.Console;
import org.openmole.ui.console.internal.command.Init;
import org.openmole.ui.console.internal.command.Print;
import org.openmole.ui.console.internal.command.Register;
import org.openmole.ui.console.internal.command.registry.Registry;

/**
 * Hello world!
 *
 */
public class Application implements IApplication {

    final public static String pluginManager = "plugin";
    final public static String structureGenerator = "structure";
    final public static String workspace = "workspace";
    final public static String registry = "registry";

    @Override
    public Object start(IApplicationContext context) throws Exception {
        // Init options parsing
        String[] args = (String[]) context.getArguments().get("application.args");
        Option optionPluginsDir = OptionBuilder.withLongOpt("pluginsDir").withDescription("Add plugins directories (seperated by \",\")").hasArgs(1).withArgName("directories").isRequired(false).create("p");
        Options options = new Options().addOption(optionPluginsDir);
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error while parsing command line arguments");
            new HelpFormatter().printHelp(" ", options);
            System.exit(1);
        }
        // Init Console
        Activator.getWorkspace().setPasswordProvider(new ForbidenPasswordProvider());
        Console console = Activator.getConsole();
        console.setVariable(pluginManager, Activator.getPluginManager());
        console.setVariable(structureGenerator, Activator.getStructureGenerator());
        console.setVariable(workspace, Activator.getWorkspace());
        console.setVariable(registry, Registry.getInstance());

        Groovysh g = console.getGroovysh();
        g.leftShift(new Print(g, "print", "\\pr"));
        g.leftShift(new Init(g, "init", "\\in"));
        g.leftShift(new Register(g, "reg", "\\rg"));

        console.run("init " + workspace);
        // Process CLI options
        if (cmd.hasOption(optionPluginsDir.getOpt())) {
            for (String directory : cmd.getOptionValue(optionPluginsDir.getOpt()).split(",")) {
                Activator.getPluginManager().loadDir(directory);
            }
        }
        // Run
        console.getGroovysh().run();
        return IApplication.EXIT_OK;
    }

    @Override
    public void stop() {
    }
}
