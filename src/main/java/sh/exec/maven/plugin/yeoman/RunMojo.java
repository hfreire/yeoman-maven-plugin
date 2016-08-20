package sh.exec.maven.plugin.yeoman;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "run", defaultPhase = LifecyclePhase.NONE)
public class RunMojo extends AbstractMojo {
    @Parameter(defaultValue = "src/main/javascript", required = false)
    private File yeomanDirectory;

    @Parameter(defaultValue = "${os.name}", readonly = true)
    private String osName;

    @Parameter(defaultValue = "server", required = true)
    private String gruntTask;

    private DefaultExecutor executor;

    public RunMojo() {
        super();

        executor = new DefaultExecutor();
    }

    public void execute() throws MojoExecutionException {
        executeGruntTask();
    }

    private void executeGruntTask() throws MojoExecutionException {
        executeCommand("grunt --no-color " + gruntTask);
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
