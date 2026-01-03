package com.drake.droidblox.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.drake.droidblox.DBApplication
import com.drake.droidblox.ui.components.BasicScreen
import com.drake.droidblox.ui.components.DropdownItem
import com.drake.droidblox.ui.components.ExtendedDropdown
import com.drake.droidblox.ui.components.ExtendedSwitch
import com.drake.droidblox.ui.components.ExtendedTextField
import com.drake.droidblox.ui.components.SectionText

@Composable
fun FFlagsScreen(
    navController: NavController? = null
) {
    /*
    follows the allowlist: https://devforum.roblox.com/t/allowlist-for-local-client-configuration-via-fast-flags/3966569
    Geometry:

    DFIntCSGLevelOfDetailSwitchingDistance
    DFIntCSGLevelOfDetailSwitchingDistanceL12
    DFIntCSGLevelOfDetailSwitchingDistanceL23
    DFIntCSGLevelOfDetailSwitchingDistanceL34

    Rendering:

    DFFlagTextureQualityOverrideEnabled
    DFIntTextureQualityOverride
    FIntDebugForceMSAASamples
    DFFlagDisableDPIScale
    FFlagDebugSkyGray
    DFFlagDebugPauseVoxelizer
    DFIntDebugFRMQualityLevelOverride
    FIntFRMMaxGrassDistance
    FIntFRMMinGrassDistance
    FFlagDebugGraphicsPreferVulkan
    FFlagDebugGraphicsPreferOpenGL

    User Interface:

    FIntGrassMovementReducedMotionFactor

    the excluded fflags for android will include the following:
    FFlagHandleAltEnterFullscreenManually
    FFlagDebugGraphicsPreferD3D11
    */

    val fflagsManager = DBApplication.instance.fastFlagsManager
    val settingsManager = DBApplication.instance.settingsManager

    val currentFFlags = fflagsManager.fflags

    BasicScreen("Fast Flags", navController, useLazyColumn = true, lazyColumnContents = {
        // god forgive me for the item lambdas
        item { ExtendedSwitch(
            "Allow DroidBlox to manage Fast Flags",
            "Disabling this will prevent anything configured here from being applied to Roblox.",
            settingsManager.applyFFlags
        ) { settingsManager.applyFFlags = it } }

        item { SectionText("Rendering") }

        //TODO("Extended Dropdown Anti-aliasing quality (MSAA)")
        item { ExtendedDropdown(
            "Anti-aliasing quality (MSAA)",
            "Smoothens the jagged edges to make textures detailed.",
            listOf(
                DropdownItem("Automatic") {
                    fflagsManager.edit { delete("FIntDebugForceMSAASamples")}
                },
                DropdownItem("1x") {
                    fflagsManager.edit { set("FIntDebugForceMSAASamples", "1") }
                },
                DropdownItem("2x") {
                    fflagsManager.edit { set("FIntDebugForceMSAASamples", "2") }
                },
                DropdownItem("4x") {
                    fflagsManager.edit { set("FIntDebugForceMSAASamples", "4") }
                }
            )
        ) }
        item { ExtendedDropdown(
            "Rendering mode",
            "Choose what rendering API to use for Roblox",
            listOf(
                DropdownItem("Automatic") {
                    fflagsManager.edit {
                        listOf(
                            "FFlagDebugGraphicsPreferVulkan",
                            "FFlagDebugGraphicsPreferOpenGL"
                        ).forEach {
                            delete(it)
                        }
                    }
                },
                DropdownItem("Vulkan") {
                    fflagsManager.edit {
                        delete("FFlagDebugGraphicsPreferOpenGL")
                        set("FFlagDebugGraphicsPreferVulkan", "true")
                    }
                },
                DropdownItem("OpenGL") { // not necessary but sstill :3
                    fflagsManager.edit {
                        delete("FFlagDebugGraphicsPreferVulkan")
                        set("FFlagDebugGraphicsPreferOpenGL", "true")
                    }
                }
            )
        ) }
        item { ExtendedDropdown(
            "Texture quality",
            "Choose what level of texture quality to render",
            items = listOf(
                DropdownItem("Automatic") {
                    fflagsManager.edit {
                        listOf(
                            "DFFlagTextureQualityOverrideEnabled",
                            "DFIntTextureQualityOverride"
                        ).forEach {
                            delete(it)
                        }
                    }
                },
                DropdownItem("Level 0") {
                    fflagsManager.edit {
                        set("DFFlagTextureQualityOverrideEnabled", "true")
                        set("DFIntTextureQualityOverride", "0")
                    }
                },
                DropdownItem("Level 1") {
                    fflagsManager.edit {
                        set("DFFlagTextureQualityOverrideEnabled", "true")
                        set("DFIntTextureQualityOverride", "1")
                    }
                },
                DropdownItem("Level 2") {
                    fflagsManager.edit {
                        set("DFFlagTextureQualityOverrideEnabled", "true")
                        set("DFIntTextureQualityOverride", "2")
                    }
                },
                DropdownItem("Level 3") {
                    fflagsManager.edit {
                        set("DFFlagTextureQualityOverrideEnabled", "true")
                        set("DFIntTextureQualityOverride", "3")
                    }
                },
            )
        ) }
        item { ExtendedSwitch(
            "Override sky to solid gray",
            "Overrides the sky into a solid gray color",
            currentFFlags["FFlagDebugSkyGray"].toBoolean()
        ) {
            fflagsManager.edit { set("FFlagDebugSkyGray", it.toString()) }
        } }
        item { ExtendedSwitch( // TODO: Improve title and subtitle
            "Pause voxelizer",
            "Pauses the voxelizer",
            currentFFlags["DBFFlagDebugPauseVoxelizer"].toBoolean()
        ) {
            fflagsManager.edit { set("DFFlagDebugPauseVoxelizer", it.toString()) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "Override quality level",
            "Overrides the quality level",
            KeyboardType.Number,
            currentFFlags["DFIntDebugFRMQualityLevelOverride"]
        ) {
            fflagsManager.edit { set("DFIntDebugFRMQualityLevelOverride", it) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "Minimum grass distance",
            "Set the minimum distance of rendering grass",
            KeyboardType.Number,
            currentFFlags["FIntFRMMinGrassDistance"]
        ) {
            fflagsManager.edit { set("FIntFRMMinGrassDistance", it) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "Maximum grass distance",
            "Set the maximum distance of rendering grass",
            KeyboardType.Number,
            currentFFlags["FIntFRMMaxGrassDistance"]
        ) {
            fflagsManager.edit { set("FIntFRMMaxGrassDistance", it) }
        } }

        item { SectionText("Geometry") }

        item { ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            currentFFlags["DFIntCSGLevelOfDetailSwitchingDistance"]
        ) {
            fflagsManager.edit { set("DFIntCSGLevelOfDetailSwitchingDistance", it) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L12",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            currentFFlags["DFIntCSGLevelOfDetailSwitchingDistanceL12"]
        ) {
            fflagsManager.edit { set("DFIntCSGLevelOfDetailSwitchingDistanceL12", it) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L23",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            currentFFlags["DFIntCSGLevelOfDetailSwitchingDistanceL23"]
        ) {
            fflagsManager.edit { set("DFIntCSGLevelOfDetailSwitchingDistanceL23", it) }
        } }
        item { ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L34",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            currentFFlags["DFIntCSGLevelOfDetailSwitchingDistanceL34"]
        ) {
            fflagsManager.edit { set("DFIntCSGLevelOfDetailSwitchingDistanceL34", it) }
        } }

        item { SectionText("User Interface") }

        item { ExtendedTextField( // TODO: Improve title and subtitle
            "Grass movement reduced motion factor",
            "Overrides the Grass movement reduced motion factor", // what the FUCK should i type
            KeyboardType.Number,
            currentFFlags["FIntGrassMovementReducedMotionFactor"]
        ) {
            fflagsManager.edit { set("FIntGrassMovementReducedMotionFactor", it) }
        } }
    })
}