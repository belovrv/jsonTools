import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf
import kotlin.reflect.full.*

class TestClass(val string: String)

@OptIn(InternalSerializationApi::class)
fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    val s = "23"
    val httpClint = HttpClient()
    httpClint.get("")
    val str =
        """
        {
            "firstName": "John",
            "lastName": "Smith",
            "isAlive": true,
            "age": 27,
            "address": {
            "streetAddress": "21 2nd Street",
            "city": "New York",
            "state": "NY",
            "postalCode": "10021-3100"
        },
            "phoneNumbers": [
            {
                "type": "home",
                "number": "212 555-1234"
            },
            {
                "type": "office",
                "number": "646 555-4567"
            }
            ],
            "children": [
            "Catherine",
            "Thomas",
            "Trevor"
            ],
            "spouse": null
        }
    """
}