package proguard.gradle

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files

class ProguardTaskRelocateabilityTest extends Specification {
    @Rule TemporaryFolder temporaryFolder
    @Rule TemporaryFolder cacheFolder

    @Unroll
    def "can cache project"() {
        def cacheDir = cacheFolder.newFolder()
        def originalDir = temporaryFolder.newFolder()
        def fixture = new File(getClass().classLoader.getResource("spring-boot").path)
        FileUtils.copyDirectory(fixture, originalDir)
        writeSettingsGradle(originalDir, cacheDir)

        def relocatedDir = temporaryFolder.newFolder()
        FileUtils.copyDirectory(fixture, relocatedDir)
        writeSettingsGradle(relocatedDir, cacheDir)

        when:
        def result = GradleRunner.create()
        .forwardOutput()
                .withArguments('proguard')
        .withProjectDir(originalDir)
        .build()
        def result2 = GradleRunner.create()
                .forwardOutput()
                .withProjectDir(originalDir)
        .withArguments('proguard')
                .build()

        print result
        print result2

        then:
        result.output =~ "SUCCESSFUL"
        result2.output =~ "SUCCESSFUL"
        result2.output =~ "task: :proguard FROM-CACHE"
    }

    def writeSettingsGradle(def projectDir, def cacheDir) {
        new File(projectDir, "settings.gradle").text = """

            plugins {
                id("com.gradle.enterprise").version("3.5")
            }
            rootProject.name = 'demo'

            gradleEnterprise {
                buildScan {
                    termsOfServiceUrl = "https://gradle.com/terms-of-service"
                    termsOfServiceAgree = "yes"
                    publishAlways()
                }
            }
            buildCache {
                local(DirectoryBuildCache) {
                    directory = "${cacheDir.absolutePath.replace(File.separatorChar, '/' as char)}"
                }
            }
            """.stripIndent()
    }
}
