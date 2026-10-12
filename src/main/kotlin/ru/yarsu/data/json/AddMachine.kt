package ru.yarsu.data.json

import com.fasterxml.jackson.annotation.JsonProperty

// D
data class AddMachine(
    @JsonProperty("Title")
    val title: String? = null,
    @JsonProperty("Type")
    val type: String? = null,
    @JsonProperty("StartDateTime")
    val startDateTime: String? = null,
    @JsonProperty("Condition")
    val condition: String? = null,
    @JsonProperty("Capacity")
    val capacity: String? = null,
    @JsonProperty("Price")
    val price: String? = null,
    @JsonProperty("Duration")
    val duration: String? = null,
    @JsonProperty("Specialist")
    val specialist: String? = null,
    @JsonProperty("Launderette")
    val launderette: String? = null,
)
