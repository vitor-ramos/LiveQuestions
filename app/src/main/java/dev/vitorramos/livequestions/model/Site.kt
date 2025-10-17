package dev.vitorramos.livequestions.model

import com.google.gson.annotations.SerializedName

sealed class Site

class SiteNotSelected : Site() {
    override fun equals(other: Any?) = this === other

    override fun hashCode() = System.identityHashCode(this)
}

class SiteData(
    @SerializedName("api_site_parameter") val apiSiteParameter: String,
    @SerializedName("name") val name: String,
    @SerializedName("audience") val audience: String,
    @SerializedName("icon_url") val iconUrl: String,
    @SerializedName("logo_url") val logoUrl: String,
    @SerializedName("styling") val styling: SiteStyling,
) : Site() {
    override fun equals(other: Any?): Boolean =
        if (other !is SiteData) false else apiSiteParameter == other.apiSiteParameter &&
                name == other.name &&
                audience == other.audience &&
                iconUrl == other.iconUrl &&
                logoUrl == other.logoUrl &&
                styling.linkColor == other.styling.linkColor &&
                styling.tagBackgroundColor == other.styling.tagBackgroundColor &&
                styling.tagForegroundColor == other.styling.tagForegroundColor

    override fun hashCode() = apiSiteParameter.length + name.length + audience.length +
            iconUrl.length + logoUrl.length + styling.linkColor.length +
            styling.tagBackgroundColor.length + styling.tagForegroundColor.length
}

class SiteStyling(
    @SerializedName("link_color") val linkColor: String,
    @SerializedName("tag_background_color") val tagBackgroundColor: String,
    @SerializedName("tag_foreground_color") val tagForegroundColor: String,
)

class SitesResponse(@SerializedName("items") val items: List<SiteData?>)
