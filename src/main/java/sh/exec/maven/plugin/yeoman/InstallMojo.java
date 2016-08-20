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

@Mojo(name = "install", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class InstallMojo extends AbstractMojo {
    @Parameter(defaultValue = "src/main/javascript", required = false)
    private File yeomanDirectory;

    @Parameter(defaultValue = "${os.name}", readonly = true)
    private String osName;

    @Parameter(defaultValue = "true", required = false)
    private Boolean npmInstall;

    @Parameter(defaultValue = "install", required = false)
    private String npmInstallCommand;

    @Parameter(defaultValue = "true", required = false)
    private Boolean bowerInstall;

    @Parameter(defaultValue = "install", required = false)
    private String bowerInstallCommand;

    @Parameter(defaultValue = "10", required = false)
    private int bowerRetries;

    private DefaultExecutor executor;

    public InstallMojo() {
        super();

        executor = new DefaultExecutor();
    }

    public void execute() throws MojoExecutionException {
        if (npmInstall) {
            executeNpmCommand();
        }

        if (bowerInstall) {
            for (int i = 0; i < bowerRetries; i++) {
                try {
                    executeBowerCommand();
                } catch (MojoExecutionException e) {
                    if (i == bowerRetries-1) {
                        throw e;
                    }
                    continue;
                }
                break;
            }
        }
    }

    private void executeNpmCommand() throws MojoExecutionException {
        executeCommand("npm " + npmInstallCommand);
    }

    private void executeBowerCommand() throws MojoExecutionException {
        executeCommand("bower --no-color " + bowerInstallCommand);
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
