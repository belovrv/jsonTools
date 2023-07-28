import org.jetbrains.kotlinx.jupyter.api.libraries.*
import org.jetbrains.kotlinx.jupyter.api.*
import wu.seal.jsontokotlin.model.TargetJsonConverter
import wu.seal.jsontokotlin.library.*
import kotlinx.serialization.json.*


fun validateJson(jsonString: String): Boolean {
    return try {
        val element = Json.Default.parseToJsonElement(jsonString)
        element !is JsonPrimitive
                && ((runCatching { element.jsonObject.entries.isNotEmpty() }.getOrNull() == true ||
                runCatching { element.jsonArray.size > 0 }.getOrNull() == true))  // JSON is valid
    } catch (e: Exception) {
        false // Invalid JSON
    }
}

fun String.convertClassToTypeAlias(): String {
    val pattern = "class (\\w+) : ArrayList<(\\w+)>\\(\\)".toRegex()
    return pattern.replace(this) { matchResult ->
        "typealias ${matchResult.groupValues[1]} = ArrayList<${matchResult.groupValues[2]}>"
    }
}


class DeserializationResult(val src: String, val className: String) {
    override fun toString() = src
}

fun String.deserialize(name: String) = DeserializationResult(this, name)


fun JupyterIntegration.Builder.initSerialization() {
    fun String.printJson() = HTML(
        """
<body>
  <style>
  json-viewer {
    /* Background, font and indentation */
    --background-color: #0000;
    --font-size: 1.5rem;
  }
  </style>
  <script src="https://unpkg.com/@alenaksu/json-viewer@2.0.0/dist/json-viewer.bundle.js"></script>
  <json-viewer id="json" data='${this.replace("\n", "")}'></json-viewer>
  <script>
        document.querySelector('#json').expandAll()
  </script>
</body>
"""
    )

    addRenderer(object : RendererHandler {
        override val execution: ResultHandlerExecution
            get() = ResultHandlerExecution { _, result ->
                FieldValue(
                    (result.value as String).printJson(),
                    result.name
                )
            }

        override fun accepts(value: Any?): Boolean {
            return value is String && validateJson(value)
        }

        override fun replaceVariables(mapping: Map<String, String>): RendererHandler {
            return this
        }
    })
    updateVariable<DeserializationResult>() { value, kProperty ->
        execute("""
            import kotlinx.serialization.decodeFromString
            import kotlinx.serialization.json.*

            ${
            JsonToKotlinBuilder().run {
                setAnnotationLib(TargetJsonConverter.Serializable).build(value.src, value.className)
            }
                .replace("Any?", "Nothing?")
                .replace("<Any>", "<Nothing>")
                .convertClassToTypeAlias()
        }
            Json.Default.decodeFromString<${value.className}>(""" + "\"\"\"" + value.src + "\"\"\"" + """)
        """).name
    }
}