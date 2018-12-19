job "caller-service" {
	datacenters = ["dc1"]
	type = "service"
	group "caller" {
		count = 2
		task "api" {
			driver = "java"
			config {
				jar_path    = "C:\\Users\\minkowp\\git\\sample-nomad-java-services-idea\\caller-service\\target\\caller-service-1.0.0-SNAPSHOT.jar"
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
				name = "caller-service"
				port = "http"
			}
		}
		restart {
			attempts = 1
		}
	}
}