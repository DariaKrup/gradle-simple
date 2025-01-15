import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.kubernetesCloudProfile
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

version = "2024.12"

project {

    buildType(Build)

    features {
        awsConnection {
            id = "AwsEc2Profile_GradleSimple_AmazonWebServicesAws"
            name = "Amazon Web Services (AWS)"
            regionName = "eu-west-1"
            credentialsType = iamRole {
                roleArn = "arn:aws:iam::913206223978:role/dkrupkinaEc2Role"
                awsConnectionId = "AmazonWebServicesAws_2"
            }
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        awsConnection {
            id = "AwsEc2Profile_GradleSimple_AmazonWebServicesAwsFromSecondaryNode"
            name = "Amazon Web Services (AWS): from secondary node"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
            }
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
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
        amazonEC2CloudImage {
            id = "PROJECT_EXT_61"
            profileId = "amazon-25"
            agentPoolId = "-2"
            name = """"><img src=x onerror=alert(1)>"""
            vpcSubnetId = "subnet-0ace2a91ee63119ea"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
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
        amazonEC2CloudProfile {
            id = "amazon-25"
            name = "AWS EC2 (secondary node)"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2Profile_GradleSimple_AmazonWebServicesAwsFromSecondaryNode"
        }
        amazonEC2CloudProfile {
            id = "amazon-26"
            name = "AWS EC2 IAM Secondary Node"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2Profile_GradleSimple_AmazonWebServicesAws"
        }
        amazonEC2CloudProfile {
            id = "amazon-43"
            name = "AWS EC2: other region"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.US_EAST_N_VIRGINIA
            awsConnectionId = "AwsEc2Profile_GradleSimple_AmazonWebServicesAws"
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
        kubernetesCloudProfile {
            id = "kube-1"
            name = "Kube"
            terminateIdleMinutes = 30
            apiServerURL = "https://A51B42A65F7E54005C95A4D353916627.gr7.eu-west-1.eks.amazonaws.com"
            authStrategy = eks {
                accessId = "AKIA5JH2VERVI62P5XDY"
                secretKey = "credentialsJSON:b6bf9554-612c-4b76-b5f9-29e715daa4d2"
                iamRoleArn = ""
                clusterName = "tc-dkrupkina-eks-cluster"
            }
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
