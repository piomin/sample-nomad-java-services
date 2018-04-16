job "callme-service" {
	datacenters = ["dc1"]
	type = "service"
	group "callme" {
		count = 2
		task "api" {
			driver = "java"
			config {
				jar_path    = "C:\\Users\\minkowp\\git\\sample-nomad-java-services\\callme-service\\target\\callme-service-1.0.0-SNAPSHOT.jar"
				jvm_options = ["-Xmx256m", "-Xms128m"]
			}
			resources {
				cpu    = 500 # MHz
				memory = 300 # MB
				network {
					port "http" {}
				}
			}
			service {
				name = "callme-service"
				port = "http"
			}
		}
		restart {
			attempts = 1
		}
	}
}