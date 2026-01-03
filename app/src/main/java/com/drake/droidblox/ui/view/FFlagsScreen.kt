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
    val fflagsManager = DBApplication.instance.fastFlagsManager
    val settingsManager = DBApplication.instance.settingsManager
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
    BasicScreen("Fast Flags", navController) {
        ExtendedSwitch(
            "Allow DroidBlox to manage Fast Flags",
            "Disabling this will prevent anything configured here from being applied to Roblox.",
            settingsManager.applyFFlags
        ) { settingsManager.applyFFlags = it }

        SectionText("Rendering")

        //TODO("Extended Dropdown Anti-aliasing quality (MSAA)")
        ExtendedDropdown(
            "Anti-aliasing quality (MSAA)",
            "Smoothens the jagged edges to make textures detailed.",
            listOf(
                DropdownItem("Automatic") {
                    fflagsManager.deleteFFlag("FIntDebugForceMSAASamples")
                },
                DropdownItem("1x") {
                    fflagsManager.setFFlag("FIntDebugForceMSAASamples", 1)
                },
                DropdownItem("2x") {
                    fflagsManager.setFFlag("FIntDebugForceMSAASamples", 2)
                },
                DropdownItem("4x") {
                    fflagsManager.setFFlag("FIntDebugForceMSAASamples", 4)
                }
            )
        )
        ExtendedDropdown(
            "Rendering mode",
            "Choose what rendering API to use for Roblox",
            listOf(
                DropdownItem("Automatic") {
                    fflagsManager.deleteFFlags(listOf(
                        "FFlagDebugGraphicsPreferVulkan",
                        "FFlagDebugGraphicsPreferOpenGL"
                    ))
                },
                DropdownItem("Vulkan") {
                    fflagsManager.deleteFFlag("FFlagDebugGraphicsPreferOpenGL")
                    fflagsManager.setFFlag("FFlagDebugGraphicsPreferVulkan", true)
                },
                DropdownItem("OpenGL") { // not necessary but sstill :3
                    fflagsManager.deleteFFlag("FFlagDebugGraphicsPreferVulkan")
                    fflagsManager.setFFlag("FFlagDebugGraphicsPreferOpenGL", true)
                }
            )
        )
        ExtendedDropdown(
            "Texture quality",
            "Choose what level of texture quality to render",
            items = listOf(
                DropdownItem("Automatic") {
                    fflagsManager.deleteFFlags(listOf(
                        "DFFlagTextureQualityOverrideEnabled",
                        "DFIntTextureQualityOverride"
                    ))
                },
                DropdownItem("Level 0") {
                    fflagsManager.setFFlag("DFFlagTextureQualityOverrideEnabled", true)
                    fflagsManager.setFFlag("DFIntTextureQualityOverride", 0)
                },
                DropdownItem("Level 1") {
                    fflagsManager.setFFlag("DFFlagTextureQualityOverrideEnabled", true)
                    fflagsManager.setFFlag("DFIntTextureQualityOverride", 1)
                },
                DropdownItem("Level 2") {
                    fflagsManager.setFFlag("DFFlagTextureQualityOverrideEnabled", true)
                    fflagsManager.setFFlag("DFIntTextureQualityOverride", 2)
                },
                DropdownItem("Level 3") {
                    fflagsManager.setFFlag("DFFlagTextureQualityOverrideEnabled", true)
                    fflagsManager.setFFlag("DFIntTextureQualityOverride", 3)
                },
            )
        )
        ExtendedSwitch(
            "Override sky to solid gray",
            "Overrides the sky into a solid gray color",
            fflagsManager.isTrue(fflagsManager.getFFlag("FFlagDebugSkyGray"))
        ) {
            fflagsManager.setFFlag("FFlagDebugSkyGray", it)
        }
        ExtendedSwitch( // TODO: Improve title and subtitle
            "Pause voxelizer",
            "Pauses the voxelizer",
            fflagsManager.isTrue(fflagsManager.getFFlag("DBFFlagDebugPauseVoxelizer"))
        ) {
            fflagsManager.setFFlag("DFFlagDebugPauseVoxelizer", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Override quality level",
            "Overrides the quality level",
            KeyboardType.Number,
            fflagsManager.getFFlag("DFIntDebugFRMQualityLevelOverride").toString()
        ) {
            fflagsManager.setFFlag("DFIntDebugFRMQualityLevelOverride", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Minimum grass distance",
            "Set the minimum distance of rendering grass",
            KeyboardType.Number,
            fflagsManager.getFFlag("FIntFRMMinGrassDistance").toString()
        ) {
            fflagsManager.setFFlag("FIntFRMMinGrassDistance", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Maximum grass distance",
            "Set the maximum distance of rendering grass",
            KeyboardType.Number,
            fflagsManager.getFFlag("FIntFRMMaxGrassDistance").toString()
        ) {
            fflagsManager.setFFlag("FIntFRMMaxGrassDistance", it)
        }

        SectionText("Geometry")

        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            fflagsManager.getFFlag("DFIntCSGLevelOfDetailSwitchingDistance").toString()
        ) {
            fflagsManager.setFFlag("DFIntCSGLevelOfDetailSwitchingDistance", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L12",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            fflagsManager.getFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL12").toString()
        ) {
            fflagsManager.setFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL12", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L23",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            fflagsManager.getFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL23").toString()
        ) {
            fflagsManager.setFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL23", it)
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L34",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            fflagsManager.getFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL34").toString()
        ) {
            fflagsManager.setFFlag("DFIntCSGLevelOfDetailSwitchingDistanceL34", it)
        }

        SectionText("User Interface")

        ExtendedTextField( // TODO: Improve title and subtitle
            "Grass movement reduced motion factor",
            "Overrides the Grass movement reduced motion factor", // what the FUCK should i type
            KeyboardType.Number,
            fflagsManager.getFFlag("FIntGrassMovementReducedMotionFactor").toString()
        ) {
            fflagsManager.setFFlag("FIntGrassMovementReducedMotionFactor", it)
        }
    }
}