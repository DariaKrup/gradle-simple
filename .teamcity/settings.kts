import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.projectFeatures.activeStorage
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.azureConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.azureStorage
import jetbrains.buildServer.configs.kotlin.projectFeatures.s3Storage
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
        awsConnection {
            id = "AmazonWebServicesAws_2"
            name = "Amazon Web Services (AWS)"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:b6bf9554-612c-4b76-b5f9-29e715daa4d2"
            }
        }
        azureConnection {
            id = "AzureArtifactsGradleSimple_AzureCloud"
            name = "Azure Cloud"
            connectionId = "AzureArtifactsGradleSimple_AzureCloud"
            credentialsType = msal {
                authorityUrl = "https://login.microsoftonline.com/080e4d1b-3521-4847-94da-37050321d1ad"
                clientId = "b6f82b73-e2ed-4a4a-8091-2f150dc41274"
                clientSecret = "credentialsJSON:68eb3c3c-c0a1-4694-af1f-92f7f89081f5"
            }
        }
        azureConnection {
            id = "AzureArtifactsGradleSimple_AzureCloudLocal"
            name = "Azure Cloud (Local)"
            connectionId = "AzureArtifactsGradleSimple_AzureCloudLocal"
            credentialsType = default()
        }
        azureStorage {
            id = "AzureLocalStorageGradle"
            storageName = "Local Azure Storage"
            connectionId = "AzureCloudMicrosoftMsal"
            accountUrl = "https://storagedkrupkina.blob.core.windows.net/"
            container = "artifacts"
            multipartThreshold = "10MB"
            multipartChunksize = "12MB"
            cdnEndpoint = "https://artifacts.azureedge.net"
        }
        activeStorage {
            id = "PROJECT_EXT_23"
            activeStorageID = "S3_atrifacts"
        }
        s3Storage {
            id = "S3_atrifacts"
            storageName = "S3 Storage"
            bucketName = "tc-dkrupkina-limited-access"
            forceVirtualHostAddressing = true
            multipartThreshold = "5 G B"
            multipartChunksize = "5 G B"
            awsEnvironment = default {
            }
            connectionId = "AmazonWebServicesAws_2"
        }
        feature {
            id = "arm-1"
            type = "CloudProfile"
            param("clientId", "b6f82b73-e2ed-4a4a-8091-2f150dc41274")
            param("secure:clientSecret", "credentialsJSON:68eb3c3c-c0a1-4694-af1f-92f7f89081f5")
            param("agent_pool_id", "-2")
            param("credentialsType", "service")
            param("description", "")
            param("cloud-code", "arm")
            param("enabled", "true")
            param("environment", "AZURE")
            param("profileId", "arm-1")
            param("name", "Azure Cloud Profile")
            param("tenantId", "080e4d1b-3521-4847-94da-37050321d1ad")
            param("subscriptionId", "759c9fa9-8b8a-4ebf-a162-52b3b8da0936")
            param("terminate-idle-time", "30")
        }
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "**/* => gradle_simple_sources.zip"

    vcs {
        root(DslContext.settingsRoot)
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
