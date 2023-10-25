import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//ini contoh class interface
// readwriteproperty itu interface bawaan
class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {
    var fieldData = initialValue
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Testing")
    val deviceA = SmartTvDevice("Samsung", "Electronic")
    deviceA.increaseSpeakerVolume()

    val deviceB = SmartLightDevice("BardiLight", "Utility")
    deviceB.increaseBrightness()
    deviceB.turnOn()
}

open class SmartDevice(val name: String, val category: String) {
    var deviceStatus = "online"

    open val deviceType = "Unknown"

    //hanya bisa diakses di dalam class dan di class turunannya
    protected constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when (statusCode) {
            0 -> "offline"
            1 -> "online"
            else -> "unknown"
        }
    }

    //open modifier agar bisa dioverride di subclass nya
    open fun turnOn() {

    }

    open fun turnOff() {

    }


}

//is a
class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

//    private var speakerVolume = 2
//        set(value) {
//            if (value in 0..100) {
//                field = value
//            }
//        }
        private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
        private var channelNumber by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
//    private var channelNumber = 2
//        set(value) {
//            if (value in 0..100) {
//                field = value
//            }
//        }

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased")
    }

    protected fun nextChannel() {
        channelNumber++
        println("change number to $channelNumber")
    }
}

//has a
class SmartHome(val smartTvDevice: SmartTvDevice) {
    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        //bisa memanggil open function dari class SmartDevice
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }

    fun turnOffTv() {
        //bisa memanggil open function dari class SmartDevice
        deviceTurnOnCount--
        smartTvDevice.turnOff()
    }
}

class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {
    override val deviceType: String
        get() = "Smart TV"
    private var brightnessLevel by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 100)
//    private var brightnessLevel = 0
//        set(value) {
//            if (value in 0..100) {
//                field = value
//            }
//        }

    fun increaseBrightness() {
        println("Current brightness : $brightnessLevel")
        brightnessLevel++
        println("Brightness increased to $brightnessLevel")
    }

    //memodifikasi method open yang ada di superclass
    override fun turnOn() {
        super.turnOn()
        deviceStatus = "on"
        println("$name turned on. brightness level $brightnessLevel")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("smart light turned off")
    }
}