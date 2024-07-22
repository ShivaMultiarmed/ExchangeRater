package mikhail.shell.exchangerater

data class ConversionData(val result: String, val conversion_rate : Double)
data class SupportedCodes (val supported_codes : List<List<String>>)