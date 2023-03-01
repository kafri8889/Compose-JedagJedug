package com.anafthdev.composejj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.core.view.WindowCompat
import com.anafthdev.composejj.data.Ref
import com.anafthdev.composejj.ui.theme.ComposeJJTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		WindowCompat.setDecorFitsSystemWindows(window, false)
		
		setContent {
			ComposeJJTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val systemUiController = rememberSystemUiController()
					
					SideEffect {
						systemUiController.setSystemBarsColor(
							color = Color.Transparent,
							darkIcons = true
						)
					}
					
					JJScreen()
				}
			}
		}
	}
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun JJScreen() {
	
	val config = LocalConfiguration.current
	
	val infiniteProgressTransition = rememberInfiniteTransition()
	val infiniteBackgroundTransition = rememberInfiniteTransition()
	
	val progress by infiniteProgressTransition.animateFloat(
		initialValue = 0f,
		targetValue = 1f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(3000),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val background by infiniteBackgroundTransition.animateColor(
		initialValue = Color.Blue,
		targetValue = Color.Green,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(250),
			repeatMode = RepeatMode.Reverse
		)
	)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.drawBehind {
				drawRect(
					color = background
				)
			}
	) {
		MotionLayout(
			progress = progress,
			motionScene = MotionScene {
				val (
					ppRef,
					centerRef,
					ropeRef,
					mp1Ref,
					devName
				) = createRefsFor(
					Ref.PhotoProfile,
					Ref.Center,
					Ref.Rope,
					Ref.MovingPhotoProfile1,
					Ref.DevName
				)
				
				defaultTransition(
					from = constraintSet {
						constrain(ppRef) {
							top.linkTo(parent.top)
							start.linkTo(parent.start)
							end.linkTo(parent.end)
						}
						
						constrain(centerRef) {
							centerVerticallyTo(parent)
							centerHorizontallyTo(parent)
						}
						
						constrain(ropeRef) {
							top.linkTo(parent.top)
							bottom.linkTo(ppRef.bottom)
							
							centerHorizontallyTo(parent)
							
							height = Dimension.fillToConstraints
							width = Dimension.value(1.dp)
						}
						
						constrain(mp1Ref) {
							top.linkTo(centerRef.top)
							bottom.linkTo(parent.bottom)
							
							start.linkTo(parent.start)
						}
						
						constrain(devName) {
							centerHorizontallyTo(parent)
							
							top.linkTo(centerRef.bottom, 16.dp)
							bottom.linkTo(parent.bottom)
						}
					},
					to = constraintSet {
						constrain(ppRef) {
							top.linkTo(parent.top)
							bottom.linkTo(parent.bottom)
							
							centerHorizontallyTo(parent)
						}
						
						constrain(centerRef) {
							centerVerticallyTo(parent)
							centerHorizontallyTo(parent)
						}
						
						constrain(ropeRef) {
							top.linkTo(parent.top)
							bottom.linkTo(ppRef.bottom)
							
							centerHorizontallyTo(parent)
							
							height = Dimension.fillToConstraints
							width = Dimension.value(1.dp)
						}
						
						constrain(mp1Ref) {
							top.linkTo(centerRef.top)
							bottom.linkTo(parent.bottom)
							
							centerHorizontallyTo(parent)
						}
						
						constrain(devName) {
							centerHorizontallyTo(parent)
							
							top.linkTo(centerRef.bottom, 16.dp)
							bottom.linkTo(parent.bottom)
						}
					}
				) {
					keyPositions(mp1Ref) {
						frame(25) {
							percentX = 30f
						}
						
						frame(100) {
							percentX = 90f
						}
					}
				}
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			PhotoProfile(
				modifier = Modifier
					.zIndex(2f)
			)
			
			Box(
				modifier = Modifier
					.background(Color.Black)
					.layoutId(Ref.Rope)
					.zIndex(1f)
			)
			
			Spacer(
				modifier = Modifier
					.layoutId(Ref.Center)
			)
			
			MovingPhotoProfile1(
				size = config.smallestScreenWidthDp.dp / 3.4f,
				modifier = Modifier
					.layoutId(Ref.MovingPhotoProfile1)
			)
			
			DevName(
				modifier = Modifier
					.layoutId(Ref.DevName)
			)
		}
		
		PhotoProfileJedagJedug(
			size = config.smallestScreenWidthDp.dp / 3,
			modifier = Modifier
				.align(Alignment.Center)
				.zIndex(3f)
		)
		
	}
}

@Composable
private fun PhotoProfile(modifier: Modifier = Modifier) {
	
	val config = LocalConfiguration.current
	
	val infiniteRotationTransition = rememberInfiniteTransition()
	
	val rotationDegrees by infiniteRotationTransition.animateFloat(
		initialValue = 0f,
		targetValue = 1800f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(1200),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	Image(
		painter = painterResource(id = R.drawable.aq_1),
		contentDescription = null,
		contentScale = ContentScale.Crop,
		modifier = modifier
			.size(config.smallestScreenWidthDp.dp / 3)
			.clip(CircleShape)
			.rotate(rotationDegrees)
			.layoutId(Ref.PhotoProfile)
	)
}

@Composable
private fun PhotoProfileJedagJedug(
	size: Dp,
	modifier: Modifier = Modifier
) {
	
	val infiniteScaleTransition = rememberInfiniteTransition()
	
	val infiniteAlpha1Transition = rememberInfiniteTransition()
	val infiniteAlpha2Transition = rememberInfiniteTransition()
	val infiniteAlpha3Transition = rememberInfiniteTransition()
	
	val infiniteSize1Transition = rememberInfiniteTransition()
	val infiniteSize2Transition = rememberInfiniteTransition()
	val infiniteSize3Transition = rememberInfiniteTransition()
	
	val scaleFactor by infiniteScaleTransition.animateFloat(
		initialValue = 0.9f,
		targetValue = 1.1f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(100),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val alpha1Factor by infiniteAlpha1Transition.animateFloat(
		initialValue = 1f,
		targetValue = 0f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(2000),
			repeatMode = RepeatMode.Restart
		)
	)
	
	val alpha2Factor by infiniteAlpha2Transition.animateFloat(
		initialValue = 1f,
		targetValue = 0f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(3000),
			repeatMode = RepeatMode.Restart
		)
	)
	
	val alpha3Factor by infiniteAlpha3Transition.animateFloat(
		initialValue = 1f,
		targetValue = 0f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(3600),
			repeatMode = RepeatMode.Restart
		)
	)
	
	val size1Factor by infiniteSize1Transition.animateFloat(
		initialValue = 1f,
		targetValue = 2f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(2000),
			repeatMode = RepeatMode.Restart
		)
	)
	
	val size2Factor by infiniteSize2Transition.animateFloat(
		initialValue = 1f,
		targetValue = 2f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(3000),
			repeatMode = RepeatMode.Restart
		)
	)
	
	val size3Factor by infiniteSize3Transition.animateFloat(
		initialValue = 1f,
		targetValue = 2f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(3600),
			repeatMode = RepeatMode.Restart
		)
	)
	
	Box(
		modifier = modifier
			.graphicsLayer {
				alpha = alpha1Factor
			}
			.border(
				width = 1.dp,
				color = Color.Black,
				shape = CircleShape
			)
			.size(size * size1Factor)
	)
	
	Box(
		modifier = modifier
			.graphicsLayer {
				alpha = alpha2Factor
			}
			.border(
				width = 1.dp,
				color = Color.Black,
				shape = CircleShape
			)
			.size(size * size2Factor)
	)
	
	Box(
		modifier = modifier
			.graphicsLayer {
				alpha = alpha3Factor
			}
			.border(
				width = 1.dp,
				color = Color.Black,
				shape = CircleShape
			)
			.size(size * size3Factor)
	)
	
	Image(
		painter = painterResource(id = R.drawable.aq_jaki),
		contentDescription = null,
		contentScale = ContentScale.Crop,
		modifier = modifier
			.size(size)
			.graphicsLayer {
				scaleX = scaleFactor
				scaleY = scaleFactor
			}
			.clip(CircleShape)
	)
}

@Composable
private fun MovingPhotoProfile1(
	size: Dp,
	modifier: Modifier = Modifier
) {
	
	Image(
		painter = painterResource(id = R.drawable.aq_2),
		contentDescription = null,
		contentScale = ContentScale.Crop,
		modifier = modifier
			.size(size)
			.clip(CircleShape)
	)
}

@Composable
private fun DevName(
	modifier: Modifier = Modifier
) {
	
	val infiniteColor1Transition = rememberInfiniteTransition()
	val infiniteColor2Transition = rememberInfiniteTransition()
	val infiniteColor3Transition = rememberInfiniteTransition()
	val infiniteShadowTransition = rememberInfiniteTransition()
	
	val infiniteScaleTransition = rememberInfiniteTransition()
	
	val color1 by infiniteColor1Transition.animateColor(
		initialValue = Color.Red,
		targetValue = Color.Yellow,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(1000),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val color2 by infiniteColor2Transition.animateColor(
		initialValue = Color.Blue,
		targetValue = Color.Magenta,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(800),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val color3 by infiniteColor3Transition.animateColor(
		initialValue = Color.Green,
		targetValue = Color.Cyan,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(1100),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val shadow by infiniteShadowTransition.animateColor(
		initialValue = Color.Black,
		targetValue = Color.Gray,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(200),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	val scaleFactor by infiniteScaleTransition.animateFloat(
		initialValue = 1f,
		targetValue = 1.4f,
		animationSpec = InfiniteRepeatableSpec(
			animation = tween(120),
			repeatMode = RepeatMode.Reverse
		)
	)
	
	Text(
		text = "@anafthdev_",
		style = MaterialTheme.typography.titleLarge.copy(
			fontWeight = FontWeight.ExtraBold
		),
		modifier = modifier
			.scale(scaleFactor)
			.graphicsLayer {
				alpha = 0.99f
			}
			.drawWithCache {
				val brush = Brush.horizontalGradient(
					listOf(
						color1,
						color2,
						color3,
					)
				)
				
				onDrawWithContent {
					drawContent()
					drawRect(brush, blendMode = BlendMode.SrcAtop)
				}
			}
	)
	
	Text(
		text = "@anafthdev_",
		style = MaterialTheme.typography.titleLarge.copy(
			fontWeight = FontWeight.ExtraBold,
			color = shadow
		),
		modifier = modifier
			.scale(scaleFactor)
			.graphicsLayer {
				alpha = 0.32f
			}
			.offset(x = 1.dp, y = 2.dp)
	)
}
