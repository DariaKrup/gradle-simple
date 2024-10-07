import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.projectFeatures.cloudIntegration
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesExecutor
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.07"

project {

    buildType(Build)

    features {
        kubernetesConnection {
            id = "PROJECT_EXT_4"
            name = "Token Kubernetes Connection"
            apiServerUrl = "https://A51B42A65F7E54005C95A4D353916627.gr7.eu-west-1.eks.amazonaws.com"
            authStrategy = token {
                token = "credentialsJSON:6e6b6dda-4306-4665-9f8e-e9f2f2df7a86"
            }
        }
        kubernetesExecutor {
            id = "PROJECT_EXT_5"
            connectionId = "PROJECT_EXT_4"
            profileName = "Kubernetes executor (jetbrains/teamcity-agent)"
            templateContainer = """
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                    - name: template-container
                    - name: agent-container
                      image: jetbrains/teamcity-agent:2024.07-linux
                      resources:
                        requests:
                          memory: "1Gi"
                        limits:
                          memory: "1.5Gi"
                      env:
                      - name: "AGENT_NAME"
                        value: "AGENT"
            """.trimIndent()
            buildsLimit = "1"
            param("profileDescription", "Kubernetes Executor with 2024.07-linux image")
            param("profileServerUrl", "http://10.128.93.57:8281/")
            param("system.cloud.profile_id", "PROJECT_EXT_5")
        }
        cloudIntegration {
            id = "PROJECT_EXT_8"
            enabled = true
            subprojectsEnabled = true
            allowOverride = false
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)

        checkoutMode = CheckoutMode.ON_SERVER
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "clean build"
            gradleWrapperPath = ""
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
