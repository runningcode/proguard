package proguard.gradle

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class ProguardTaskRelocateabilityTest extends Specification {
    @Rule TemporaryFolder temporaryFolder

    @Unroll
    def "can cache project"() {
        def originalDir = temporaryFolder.newFolder()
        def settings = file("settings.gradle").text = "foo"
        file($buildDir)

        def relocatedDir = temporaryFolder.newFolder()
//        when:
//        def foo = "bar"

//        then:
        foo == "bar"
//        file("settings.gradle").text contains "foo"
    }

}
