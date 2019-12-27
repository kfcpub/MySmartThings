metadata {
	definition(name: "Hampton Bay Fan Controller", namespace: "kfcpub", author: "kfcpub", ocfDeviceType: "oic.d.fan", genericHandler: "Zigbee") {
        capability "Configuration"
        capability "Refresh"
		capability "Switch Level"
		capability "Switch"
        capability "Light"
		capability "Fan Speed"
		capability "Health Check"
		capability "Actuator"
		capability "Refresh"
        capability "Polling"
        capability "Sensor"
        capability "Color Control"

		command "low"
		command "medium"
		command "mediumHigh"
		command "high"
		command "breeze"
        command "setFanSpeed"  
        command "setLevel"  
        command "setColor"  
        command "fanOn"  
        command "fanOff"  

        attribute "lastFanSpeed", "Number"		//used to restore previous fanmode
        attribute "lastLevel", "String"		//used to restore previous light level

		fingerprint profileId: "0104", inClusters: "0000, 0003, 0004, 0005, 0006, 0008, 0202", outClusters: "0003, 0019", model: "HDC52EastwindFan"
	}

	/*simulator {
		status "00%": "command: 2003, payload: 00"
		status "33%": "command: 2003, payload: 21"
		status "66%": "command: 2003, payload: 42"
		status "99%": "command: 2003, payload: 63"
	}*/

	tiles(scale: 2) {
		multiAttributeTile(name: "fanSpeed", type: "generic", width: 6, height: 4, canChangeIcon: true) {
			tileAttribute("device.fanSpeed", key: "PRIMARY_CONTROL") {
				attributeState "0", action:"fanOn", label: "off", icon: "st.thermostat.fan-off", backgroundColor: "#ffffff"
				attributeState "1", action:"fanOff", label: "low", icon: "st.thermostat.fan-on", backgroundColor: "#00a0dc"
				attributeState "2", action:"fanOff", label: "medium", icon: "st.thermostat.fan-on", backgroundColor: "#00a0dc"
				attributeState "3", action:"fanOff", label: "medium-high", icon: "st.thermostat.fan-on", backgroundColor: "#00a0dc"
				attributeState "4", action:"fanOff", label: "high", icon: "st.thermostat.fan-on", backgroundColor: "#00a0dc"
				attributeState "6", action:"fanOff", label: "breeze", icon: "st.thermostat.fan-on", backgroundColor: "#00a0dc"
			}
			tileAttribute("device.fanSpeed", key: "VALUE_CONTROL") {
				attributeState "fanSpeed", action: "setFanSpeed"
			}
		}
        
        standardTile("lowFanSpeed", "fanSpeed", decoration: "flat", width: 1, height: 1) {  
     		state "default", label:"LOW", action: "low", backgroundColor: "#ffffff", nextState: "turningOn"
            state "1", label: "LOW", action: "off", backgroundColor: "#79b821", nextState: "turningOff"
        	state "turningOn", label:"ADJUSTING", action: "on", backgroundColor: "#2179b8", nextState: "turningOn"
            state "turningOff", label:"TURNING OFF", action:"off", backgroundColor:"#2179b8", nextState: "turningOff"
        }
        
        standardTile("mediumFanSpeed", "fanSpeed", decoration: "flat", width: 1, height: 1) {  
     		state "default", label:"MED", action: "medium", backgroundColor: "#ffffff", nextState: "turningOn"
            state "2", label: "MED", action: "off", backgroundColor: "#79b821", nextState: "turningOff"
        	state "turningOn", label:"ADJUSTING", action: "on", backgroundColor: "#2179b8", nextState: "turningOn"
            state "turningOff", label:"TURNING OFF", action:"off", backgroundColor:"#2179b8", nextState: "turningOff"
        }
        
        standardTile("mediumHighFanSpeed", "fanSpeed", decoration: "flat", width: 1, height: 1) {  
     		state "default", label:"MED-HI", action: "mediumHigh", backgroundColor: "#ffffff", nextState: "turningOn"
            state "3", label: "MED-HI", action: "off", backgroundColor: "#79b821", nextState: "turningOff"
        	state "turningOn", label:"ADJUSTING", action: "on", backgroundColor: "#2179b8", nextState: "turningOn"
            state "turningOff", label:"TURNING OFF", action:"off", backgroundColor:"#2179b8", nextState: "turningOff"
        }
        
        standardTile("highFanSpeed", "fanSpeed", decoration: "flat", width: 1, height: 1) {  
     		state "default", label:"HIGH", action: "high", backgroundColor: "#ffffff", nextState: "turningOn"
            state "4", label: "HIGH", action: "off", backgroundColor: "#79b821", nextState: "turningOff"
        	state "turningOn", label:"ADJUSTING", action: "on", backgroundColor: "#2179b8", nextState: "turningOn"
            state "turningOff", label:"TURNING OFF", action:"off", backgroundColor:"#2179b8", nextState: "turningOff"
        }
        
        standardTile("breezeFanSpeed", "fanSpeed", decoration: "flat", width: 1, height: 1) {  
     		state "default", label:"BREEZE", action: "breeze", backgroundColor: "#ffffff", nextState: "turningOn"
            state "6", label: "BREEZE", action: "off", backgroundColor: "#79b821", nextState: "turningOff"
        	state "turningOn", label:"ADJUSTING", action: "on", backgroundColor: "#2179b8", nextState: "turningOn"
            state "turningOff", label:"TURNING OFF", action:"off", backgroundColor:"#2179b8", nextState: "turningOff"
        }
        
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            tileAttribute ("level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"setLevel"
			}
		}
		standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label: '', action: "refresh.refresh", icon: "st.secondary.refresh"
		}
		main "fanSpeed"
		details(["fanSpeed", "lowFanSpeed", "mediumFanSpeed", "mediumHighFanSpeed", "highFanSpeed", "breezeFanSpeed", "switch", "refresh"])
	}

}

def installed() {	
	initialize()
}
def initialize () {
   response(configure())
}
def updated() {
	//if(state.oldLabel != device.label) {updateChildLabel()}
		initialize()    
}

def configure() {
	log.info "Configuring Reporting and Bindings."
	def cmd = 
    [
	  //Set long poll interval
	  "raw 0x0020 {11 00 02 02 00 00 00}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  //Bindings for Fan Control
      "zdo bind 0x${device.deviceNetworkId} 1 1 0x006 {${device.zigbeeId}} {}", "delay 100",
      "zdo bind 0x${device.deviceNetworkId} 1 1 0x008 {${device.zigbeeId}} {}", "delay 100",
	  "zdo bind 0x${device.deviceNetworkId} 1 1 0x202 {${device.zigbeeId}} {}", "delay 100",
	  //Fan Control - Configure Report
      "zcl global send-me-a-report 0x006 0 0x10 1 300 {}", "delay 100",
       "send 0x${device.deviceNetworkId} 1 1", "delay 100",
      "zcl global send-me-a-report 0x008 0 0x20 1 300 {}", "delay 100",
       "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  "zcl global send-me-a-report 0x202 0 0x30 1 300 {}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  //Update values
      "st rattr 0x${device.deviceNetworkId} 1 0x006 0", "delay 100",
      "st rattr 0x${device.deviceNetworkId} 1 0x008 0", "delay 100",
	  "st rattr 0x${device.deviceNetworkId} 1 0x202 0", "delay 100",
	 //Set long poll interval
	  "raw 0x0020 {11 00 02 1C 00 00 00}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100"
	]
    return cmd + refresh()
}

def parse(String description) {
    def result = []
    log.debug "Parse description $description"           
    def event = zigbee.getEvent(description)
    log.debug "event $event"
    if (event) {
    	log.info "Light event detected on controller: ${event}"
        if (event.name=="level" && event.value==0) {
            if(event.value != "on" && event.value != "off") result << createEvent(name: "level", value: event.value)
        } else {
			sendEvent(event)
		}
    }
	else {
       	log.info "Fan event detected on controller: ${event}"

		if (description?.startsWith("read attr -")) {
			def descMap = zigbee.parseDescriptionAsMap(description)
            //log.info "cluster " + descMap.cluster
            //log.info "attrId " + descMap.attrId
			if (descMap.cluster == "0202" && descMap.attrId == "0000") {     // Fan Control Cluster Attribute Read Response       
            	result << fanEvents(descMap.value)
			} 
		}	// End of Read Attribute Response
		return result 
   	}                
}

def fanEvents(rawSpeed) {
	def speedInt = rawSpeed as int
	def result = []

	if (speedInt >= 0 && speedInt <= 4) {
        //if (0 == speedInt) {
        //}
        
		//def onOff = (speedInt ? "on" : "off")
		//result << createEvent(name: "switch", value: onOff)
		//result << createEvent(name: "level", value: rawLevel == 99 ? 100 : rawLevel)
		result << createEvent(name: "fanSpeed", value: speedInt)
	}

	return result
}

def setColor(color) {
	log.info "In setColor $color"
    if (17 == color.hue && 6 == color.saturation) {
    	low()
    } else if (10 == color.hue && 32 == color.saturation) {
    	medium()
    } else if (17 == color.hue && 16 == color.saturation) {
    	mediumHigh()
    } else if (8 == color.hue && 59 == color.saturation) {
    	high()
    } else if (8 == color.hue && 66 == color.saturation) {
    	breeze()
    } else {
    	fanOff()
    }
}

def setFanSpeed(speed) {
	log.info "In setFanSpeed $speed"
    setFanLevel(speed.toInteger())
    //return result
}

def setFanLevel(speedInt) {
    log.info("in setFanLevel $speedInt")
    
    if (speedInt == 5) {
    	speedInt = 6
    }
      
	if ((speedInt >= 0 && speedInt <= 4) || speedInt == 6) {
    	def result = []
        def cmds=[
            "st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {0${speedInt}}"
        ]
        log.info "Adjusting Fan Speed to "+ getFanName()[speedInt]
        sendEvent("name":"fanSpeed", "value": speedInt)
        //cmds << createEvent(name:"fanSpeed", value:speedInt)
        return cmds
    } else {
    	log.info "Fan level requested was "+ getFanName()[speedInt] + " which is not handled yet"
    }
}

def setLevel(level, rate = null) {
	log.info "In setLevel $level"
    def result = []
    result << zigbee.setLevel(level) + (level?.toInteger() > 0 ? zigbee.on() : [:]) + refresh()
    return result
}

def low() {
	setFanLevel(1)
}

def medium() {
	setFanLevel(2)
}

def mediumHigh() {
	setFanLevel(3)
}

def high() {
	setFanLevel(4)
}

def breeze() {
	setFanLevel(6)
}

def getFanName() { 
	[  
    0:"Off",
    1:"Low",
    2:"Med",
    3:"Med-Hi",
	4:"High",
    5:"Off",
    6:"Comfort Breeze™",
    7:"Light"
	]
}

def fanOn() {
	log.info "Resuming Previous Fan Speed"   
	def lastFanSpeed =  device.currentValue("lastFanSpeed")	 //resumes previous fanspeed
	return setFanLevel(lastFanSpeed ? lastFanSpeed : 1)
    
}

def fanOff() {	
    def speedInt = device.currentValue("fanSpeed")    //save fanspeed before turning off so it can be resumed when turned back on
    log.info "Turning fan Off"    
    sendEvent("name":"lastFanSpeed", value: speedInt)
    setFanLevel(0)
}

def on() {
	log.info ("In on");
  	def result = []
	result << zigbee.command(0x0006, 0x01)
	result << sendEvent(name: "switch", value: "on")
	return result 
}

def off() {
	log.info ("In off");
    def result = []
    result << zigbee.command(0x0006, 0x00)
    result << sendEvent(name: "switch", value: "off")
    return result 
}

def ping() {
	refresh()
}

def refresh() {	
	zigbee.onOffRefresh() + zigbee.levelRefresh() + zigbee.readAttribute(0x0202, 0x0000)
}