package de.fxdiagram.examples.java

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.Separator
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class JavaTypeNode extends XNode {
	
	val propertyCompartment = new VBox
	val operationCompartment = new VBox
	
	JavaTypeModel model
	
	new(JavaTypeDescriptor domainObject) {
		super(domainObject)
	}
	
	protected override createNode() {
		new RectangleBorderPane => [
			children += new VBox => [
				children += new Text => [
					text = javaType.simpleName
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				children += new Separator 
				children += propertyCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				children += new Separator
				children += operationCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				alignment = Pos.CENTER
			]
		]
	}
	
	def getJavaType() {
		(domainObject as JavaTypeDescriptor).domainObject
	}
	
	def getJavaTypeModel() {
		if(model == null) 
			model = new JavaTypeModel(javaType)
		model
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def populateCompartments() {
		javaTypeModel.properties.limit.forEach [
			property |
			propertyCompartment.children += new Text => [
				text = '''«property.name»: «property.type.simpleName»''' 
			]
		]
		javaTypeModel.constructors.forEach [
			constructor |
			operationCompartment.children += new Text => [
				text = '''«javaType.simpleName»(«constructor.parameterTypes.map[simpleName].join(', ')»)''' 
			]
		]
		javaTypeModel.operations.limit.forEach [
			method |
			operationCompartment.children += new Text => [
				text = '''«method.name»(«method.parameterTypes.map[simpleName].join(', ')»): «method.returnType.simpleName»''' 
			]
		]
	}
	
	protected def <T> limit(List<T> list) {
		if(list.empty)
			list
		else if(isActive)
			list
		else 
			list.subList(0, Math.min(list.size, 4))
	}
	
	override doActivate() {
		super.doActivate
		populateCompartments
		addBehavior(new AddSuperTypeRapidButtonBehavior(this))
		addBehavior(new AddReferenceRapidButtonBehavior(this))
	}
	
	override toString() {
		javaType.simpleName
	}
	
}

