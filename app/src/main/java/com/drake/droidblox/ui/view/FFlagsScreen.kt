package com.drake.droidblox.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
    //val settingsManager = DBApplication.instance.settingsManager
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
            //settingsManager.applyFFlags
        ) { /*settingsManager.applyFFlags = it*/ }

        SectionText("Rendering")

        //TODO("Extended Dropdown Anti-aliasing quality (MSAA)")
        ExtendedDropdown(
            "Anti-aliasing quality (MSAA)",
            "Smoothens the jagged edges to make textures detailed.",
            listOf(
                DropdownItem("Automatic") {
                    // delete FIntDebugForceMSAASamples
                },
                DropdownItem("1x") {
                    // write FIntDebugForceMSAASamples 1
                },
                DropdownItem("2x") {
                    // write FIntDebugForceMSAASamples 2
                },
                DropdownItem("4x") {
                    // write FIntDebugForceMSAASamples 4
                }
            )
        )
        ExtendedDropdown(
            "Rendering mode",
            "Choose what rendering API to use for Roblox",
            listOf(
                DropdownItem("Automatic") {
                    // delete FFlagDebugGraphicsPreferVulkan and FFlagDebugGraphicsPreferOpenGL
                },
                DropdownItem("Vulkan") {
                    // delete FFlagDebugGraphicsPreferOpenGL
                    // write FFlagDebugGraphicsPreferVulkan
                },
                DropdownItem("OpenGL") { // not necessary but sstill :3
                    // delete FFlagDebugGraphicsPreferVulkan
                    // write FFlagDebugGraphicsPreferOpenGL
                }
            )
        )
        ExtendedDropdown(
            "Texture quality",
            "Choose what level of texture quality to render",
            items = listOf(
                DropdownItem("Automatic") {
                    // delete DFFlagTextureQualityOverrideEnabled DFIntTextureQualityOverride
                },
                DropdownItem("Level 0") {
                    // set DFFlagTextureQualityOverrideEnabled True
                    // set DFIntTextureQualityOverride 0
                },
                DropdownItem("Level 1") {
                    // set DFFlagTextureQualityOverrideEnabled True
                    // set DFIntTextureQualityOverride 1
                },
                DropdownItem("Level 2") {
                    // set DFFlagTextureQualityOverrideEnabled True
                    // set DFIntTextureQualityOverride 2
                },
                DropdownItem("Level 3") {
                    // set DFFlagTextureQualityOverrideEnabled True
                    // set DFIntTextureQualityOverride 3
                },
            )
        )
        ExtendedSwitch(
            "Override sky to solid gray",
            "Overrides the sky into a solid gray color",
            // todo
        ) {
            // FFlagDebugSkyGray
        }
        ExtendedSwitch( // TODO: Improve title and subtitle
            "Pause voxelizer",
            "Pauses the voxelizer",
            // todo
        ) {
            // DFFlagDebugPauseVoxelizer
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Override quality level",
            "Overrides the quality level",
            KeyboardType.Number,
            // todo
        ) {
            // DFIntDebugFRMQualityLevelOverride
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Minimum grass distance",
            "Set the minimum distance of rendering grass",
            KeyboardType.Number,
            // todo
        ) {
            // FIntFRMMinGrassDistance
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "Maximum grass distance",
            "Set the maximum distance of rendering grass",
            KeyboardType.Number,
            // todo
        ) {
            // FIntFRMMaxGrassDistance
        }

        SectionText("Geometry")

        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            // todo
        ) {
            // DFIntCSGLevelOfDetailSwitchingDistance
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L12",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            // todo
        ) {
            // DFIntCSGLevelOfDetailSwitchingDistanceL12
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L23",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            // todo
        ) {
            // DFIntCSGLevelOfDetailSwitchingDistanceL23
        }
        ExtendedTextField( // TODO: Improve title and subtitle
            "LOD for Polygons L34",
            "Overrides the LOD (Level of Detail) per stud",
            KeyboardType.Number,
            // todo
        ) {
            // DFIntCSGLevelOfDetailSwitchingDistanceL34
        }

        SectionText("User Interface")

        ExtendedTextField( // TODO: Improve title and subtitle
            "Grass movement reduced motion factor",
            "Overrides the Grass movement reduced motion factor", // what the FUCK should i type
            KeyboardType.Number,
            // todo
        ) {
            // FIntGrassMovementReducedMotionFactor
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    FFlagsScreen()
}