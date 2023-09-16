package com.loc.shaderfab

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout

data class FloatingActionButtonOption(
    @DrawableRes val icon: Int,
    val text: String,
    val tintColor: Color,
    val containerColor: Color,
    val textColor: Color,
)

data class FloatingActionButtonSizes(
    val mainFloatingActionButton: Dp = 57.dp,
    val optionFloatingActionButton: Dp = 46.dp,
)

val blue = Color(0xFF1982DD)

private val defaultShaderLayerColor = Color.Black.copy(alpha = 0.5f)


@Composable
fun InteractiveFloatingActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes mainIcon: Int,
    @DrawableRes mainIconAlternative: Int,
    mainIconColor: Color = Color.White,
    mainContainerColor: Color = blue,
    mainText: String,
    textStyle: TextStyle = TextStyle(color = Color.Black),
    options: List<FloatingActionButtonOption>,
    sizes: FloatingActionButtonSizes = FloatingActionButtonSizes(),
    showOptions: Boolean = false,
    shaderLayerColor: Color = defaultShaderLayerColor,
    dismissFABOptionsRequest: (() -> Unit)? = null, // This is used to change the showOptions boolean to false
    onMainIconClick: () -> Unit,
    onOptionClick: (FloatingActionButtonOption) -> Unit
) {
    // The Shader layer could be accomplished via a Dialog
    var fabTopLeftOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    val conf = LocalConfiguration.current
    val density = LocalDensity.current
    val paddingValues = remember(fabTopLeftOffset) {
        with(density) {
            val endPadding = (conf.screenWidthDp.dp - fabTopLeftOffset.x.toDp())
            val bottomPadding = (conf.screenHeightDp.dp - fabTopLeftOffset.y.toDp())
            PaddingValues(end = endPadding, bottom = bottomPadding)
        }
    }
    AnimatedVisibility(
        visible = showOptions,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Dialog(
            onDismissRequest = { dismissFABOptionsRequest?.invoke() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val layoutDirection = LocalLayoutDirection.current
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = shaderLayerColor)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { dismissFABOptionsRequest?.invoke() })
            ) {
                Row(
                    modifier = Modifier
                        .constrainAs(ref = ConstrainedLayoutReference("fabRow")) {
                            bottom.linkTo(
                                parent.bottom,
                                margin = paddingValues.calculateBottomPadding() - sizes.mainFloatingActionButton
                            )
                            end.linkTo(
                                parent.end,
                                margin = paddingValues.calculateEndPadding(layoutDirection) - sizes.mainFloatingActionButton
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Texts Column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (showOptions) {
                            options.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .height(sizes.optionFloatingActionButton),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = option.text, style = textStyle)
                                }
                            }
                            // Main Button Text
                            Box(
                                modifier = Modifier.height(sizes.mainFloatingActionButton),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = mainText, style = textStyle)
                            }
                        }
                    }
                    if (showOptions) {
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    // Icons Column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ) {
                        var triggerMainFabAnimation by remember {
                            mutableStateOf(false)
                        }
                        val mainIconRotationValue by animateFloatAsState(
                            targetValue = if (triggerMainFabAnimation) 0f else -180f,
                            label = "Fab Rotation",
                            animationSpec = tween(200)
                        )
                        val scaleValue by animateFloatAsState(
                            targetValue = if (triggerMainFabAnimation) 1.1f else 1f,
                            label = "Fab Scale",
                            animationSpec = tween(50)
                        )
                        LaunchedEffect(key1 = true) {
                            triggerMainFabAnimation = true
                        }
                        //Option Floating Action Button
                        options.forEach { option ->
                            AnimatedFloatingActionButton(
                                isVisible = showOptions,
                                size = sizes.optionFloatingActionButton,
                                containerColor = option.containerColor,
                                tintColor = option.tintColor,
                                icon = option.icon,
                                onClick = { onOptionClick(option) }
                            )
                        }
                        //Main Floating Action Button
                        FloatingActionButton(
                            modifier = Modifier
                                .size(sizes.mainFloatingActionButton)
                                .rotate(mainIconRotationValue)
                                .scale(scaleValue),
                            containerColor = mainContainerColor,
                            contentColor = mainIconColor,
                            shape = CircleShape,
                            onClick = {
                                onMainIconClick()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = if (showOptions) mainIconAlternative else mainIcon),
                                modifier = Modifier.scale(scaleValue),
                                contentDescription = null
                            )
                        }
                    }
                }
            }


        }
    }
    if (!showOptions) {
        FloatingActionButton(
            modifier = modifier
                .size(sizes.mainFloatingActionButton)
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInRoot()
                    fabTopLeftOffset = rect.topLeft
                },
            containerColor = mainContainerColor,
            contentColor = mainIconColor,
            shape = CircleShape,
            onClick = {
                onMainIconClick()
            },
        ) {
            Icon(
                painter = painterResource(id = if (showOptions) mainIconAlternative else mainIcon),
                modifier = Modifier,
                contentDescription = null
            )
        }
    }


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedFloatingActionButton(
    isVisible: Boolean,
    size: Dp,
    containerColor: Color,
    tintColor: Color,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(tween(200)),
        exit = scaleOut(tween(200))
    ) {
        FloatingActionButton(
            modifier = Modifier.size(size),
            shape = CircleShape,
            containerColor = containerColor,
            onClick = onClick
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(tween(300)),
                exit = scaleOut(tween(300)),
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = tintColor
                )
            }
        }
    }
}