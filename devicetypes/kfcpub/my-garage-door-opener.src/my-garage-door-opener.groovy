/**
 *  Z-Wave Garage Door Opener
 *
 *  Copyright 2014 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "My Garage Door Opener", namespace: "kfcpub", ocfDeviceType: "x.com.st.doorcontrol", author: "Kent Carpenter") {
		capability "Actuator"
		capability "Door Control"
        //capability "Garage Door Control"
		//capability "Contact Sensor"
		capability "Refresh"
		//capability "Sensor"
		capability "Health Check"
		//capability "Switch"
	}
    
    //attribute "refreshFromContact", "number"
    
    command "setAsOpen"
    command "setAsClosed"
    command "on"
    command "off"
    //command "open"
    //command "close"
    
	simulator {
		
	}

	tiles {
		standardTile("toggle", "device.door", width: 2, height: 2) {
			state("closed", label:'${name}', action:"door control.open", icon:"st.doors.garage.garage-closed", backgroundColor:"#00A0DC", nextState:"opening")
			state("open", label:'${name}', action:"door control.close", icon:"st.doors.garage.garage-open", backgroundColor:"#e86d13", nextState:"closing")
			state("opening", label:'${name}', icon:"st.doors.garage.garage-closed", backgroundColor:"#e86d13")
			state("closing", label:'${name}', icon:"st.doors.garage.garage-open", backgroundColor:"#00A0DC")
			
		}
		standardTile("open", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'open', action:"door control.open", icon:"st.doors.garage.garage-opening"
		}
		standardTile("close", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'close', action:"door control.close", icon:"st.doors.garage.garage-closing"
		}
		standardTile("refresh", "device.refresh", width: 1, height: 1) {
			state "default", label:'Refresh', action: "refresh", icon:"st.secondary.refresh-icon"
		}

		main "toggle"
		details(["toggle", "open", "close", "refresh"])
	}
}

def parse(String description) {
	log.trace "parse($description)"
}

def open() {
	log.debug "open"
	sendEvent(name: "door", value: "opening", data: [method: "app"])
}

def refresh() {
	log.debug "refresh"
	sendEvent(name: "door", value: "unknown", data: [method: "app"])
}

def close() {
	log.debug "close"
	sendEvent(name: "door", value: "closing", data: [method: "app"])
}

def on() {
	log.debug "on/open"
	sendEvent(name: "door", value: "opening", data: [method: "Google"])
}

def off() {
	log.debug "off/closed"
	sendEvent(name: "door", value: "closing", data: [method: "Google"])
}
    
def setAsOpen() {
	log.debug "setAsOpen"
	sendEvent(name: "door", value: "open")
    //sendEvent(name: "switch", value: "on")
}

def setAsClosed() {
	log.debug "setAsClosed"
	sendEvent(name: "door", value: "closed")
    //sendEvent(name: "switch", value: "off")
}

def installed() {
	log.trace "Executing 'installed'"
	initialize()
}

def updated() {
	log.trace "Executing 'updated'"
	initialize()
}

private initialize() {
	log.trace "Executing 'initialize'"

	sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
	sendEvent(name: "healthStatus", value: "online")
	sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
}