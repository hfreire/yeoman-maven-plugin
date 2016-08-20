package sh.exec.maven.plugin.yeoman;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "clean")
public class CleanMojo extends AbstractMojo {
    @Parameter(defaultValue = "src/main/javascript", required = false)
    private File yeomanDirectory;

    @Parameter(defaultValue = "${os.name}", readonly = true)
    private String osName;

    @Parameter(defaultValue = "cache clean", required = false)
    private String bowerCommand;

    private DefaultExecutor executor;

    public CleanMojo() {
        super();

        executor = new DefaultExecutor();
    }

    public void execute() throws MojoExecutionException {
        executeBowerCommand();
    }

    private void executeBowerCommand() throws MojoExecutionException {
        executeCommand("bower --no-color " + bowerCommand);
    }

    private void executeCommand(String command) throws MojoExecutionException {
        try {
            if (osName.startsWith("Windows"))
                command = "cmd /c " + command;
            
            CommandLine commandLine = CommandLine.parse(command);
            executor.setWorkingDirectory(yeomanDirectory);
            executor.execute(commandLine);
            
        } catch (IOException e) {
            throw new MojoExecutionException("Error during: " + command, e);
        }
    }
}
