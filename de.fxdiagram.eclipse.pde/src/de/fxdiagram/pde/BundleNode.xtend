package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.mapping.shapes.BaseNode
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings
import javafx.animation.ParallelTransition
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.osgi.service.resolver.BundleDescription

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import javafx.geometry.Side

@ModelNode('inflated')
class BundleNode extends BaseNode<BundleDescription> implements INodeWithLazyMappings {

	@FxProperty boolean inflated = false

	Pane contentArea
	
	VBox titleArea
	Text nameLabel
	Text versionLabel
	
	VBox detailsArea
	
	Inflator titleInflator
	
	Inflator detailsInflator
	
	new(BundleDescriptor descriptor) {
		super(descriptor)
		placementHint = Side.BOTTOM
	}
	
	override BundleDescriptor getDomainObjectDescriptor() {
		super.domainObjectDescriptor as BundleDescriptor
	}
	
	override createNode() {
		new RectangleBorderPane => [
			tooltip = 'Right-click to toggle details'
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				children += titleArea = new VBox => [
					alignment = Pos.CENTER
					children += nameLabel = new Text => [
						textOrigin = VPos.TOP
						text = domainObjectDescriptor.symbolicName
						val isSingleton = domainObjectDescriptor.withDomainObject[
							isSingleton
						]
						if(isSingleton) 
							font = Font.font(font.family, FontWeight.BOLD, FontPosture.ITALIC, font.size * 1.1)
						else 
							font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
					]
				]
			]
			val backgroundStops = 
				if(domainObjectDescriptor.withPlugin[isFragmentModel]) 
					#[
						new Stop(0, Color.rgb(255, 193, 210)), 
						new Stop(1, Color.rgb(255, 215, 215))
					]
			else
					#[
						new Stop(0, Color.rgb(158, 188, 227)), 
						new Stop(1, Color.rgb(220, 230, 255))
					]
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				backgroundStops)
		]
	}
	
	override doActivate() {
		super.doActivate()
		titleInflator = new Inflator(this, titleArea)
		titleInflator.addInflatable(new VBox => [
			alignment = Pos.CENTER
			children += versionLabel = new Text => [
				textOrigin = VPos.TOP
				text = domainObjectDescriptor.version
				font = Font.font(font.family, font.size * 0.8)
			]
		], 1)
		detailsInflator = new Inflator(this, contentArea)
		detailsArea = new VBox => [
			padding = new Insets(10,0,0,0)
			alignment = Pos.CENTER
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObjectDescriptor.withPlugin[
					pluginBase.getResourceString(pluginBase.name)
				]
			]
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObjectDescriptor.withPlugin[
					pluginBase.getResourceString(pluginBase.providerName)
				]
			]
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObjectDescriptor.withDomainObject[
					executionEnvironments.join(', ')
				]
			]
		]
		detailsInflator.addInflatable(detailsArea, 1)
		onMouseClicked = [
			if(button == MouseButton.SECONDARY) 
				toggleInflated
		]
		val dependencyPathAction = new AddDependencyPathAction(false)
		val rapidButtonBehavior = getBehavior(LazyConnectionMappingBehavior) 
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(TOP, 'Add dependency path'), dependencyPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(BOTTOM, 'Add dependency path'), dependencyPathAction))
		
		val inverseDependencyPathAction = new AddDependencyPathAction(true)
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(BOTTOM, 'Add inverse dependency path'), inverseDependencyPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(TOP, 'Add inverse dependency path'), inverseDependencyPathAction))
	}
	
	protected def toggleInflated() {
		if(!inflated) {
			inflated = true
			new ParallelTransition => [
				children += titleInflator.inflateAnimation
				children += detailsInflator.inflateAnimation
				play
			]		
		} else {
			inflated = false
			new ParallelTransition => [
				children += titleInflator.deflateAnimation
				children += detailsInflator.deflateAnimation
				play
			]
		}
	}
	
	override getButtonSides(ConnectionMapping<?> mapping) {
		#[ LEFT, RIGHT ]
	}
	
}