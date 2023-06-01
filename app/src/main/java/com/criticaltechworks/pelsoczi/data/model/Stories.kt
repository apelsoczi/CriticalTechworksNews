package com.criticaltechworks.pelsoczi.data.model

/**
 * Representation of repository responses and repository data.
 */
sealed interface Stories {

    /**
     * Top Stories response with data.
     */
    data class Headlines(
        val headlines: List<Headline>
    ) : Stories

    /**
     * Server api error message, or just no stories available for the news source.
     */
    object NoContent : Stories

    /**
     * Communication between the device and server is down.
     */
    object Offline : Stories

}