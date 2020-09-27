package dev.vitorramos.livequestions.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

sealed class Site

class SiteNotSelected : Site() {
    override fun equals(other: Any?) = this === other

    override fun hashCode() = System.identityHashCode(this)
}

@Entity
class SiteData(
    @SerializedName("api_site_parameter") @PrimaryKey val apiSiteParameter: String,
    @SerializedName("name") @ColumnInfo val name: String,
    @SerializedName("audience") @ColumnInfo val audience: String,
    @SerializedName("icon_url") @ColumnInfo val iconUrl: String,
    @SerializedName("logo_url") @ColumnInfo val logoUrl: String,
    @SerializedName("styling") @Embedded val styling: SiteStyling,
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

internal class SitesResponse(@SerializedName("items") val items: List<SiteData?>)
