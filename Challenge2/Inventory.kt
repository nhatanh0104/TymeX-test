fun main() {
    val laptop = Product("Laptop", 999.99, 5)
    val smartphone = Product("Smartphone", 499.99, 10)
    val tablet = Product("Tablet", 299.99, 0)
    val smartwatch = Product("Smartwatch", 199.99, 3)
    
    val inventory = Inventory(listOf(laptop, smartphone, tablet, smartwatch))
    
    println(inventory.totalValue())
    println(inventory.findMostExpensive())
    println(inventory.isInstock("laptop"))
}

data class Product(
    val name: String,
    val price: Double,
    val quantity: Int,
)

data class Inventory(
    val products: List<Product>
) {
    // Calculate the total inventory value
    fun totalValue(): Double {
        val total = products.fold(0.0) { acc, product ->
            acc + product.price * product.quantity
        }
        return total
    }
    
    // Find the mist expensive product
    fun findMostExpensive(): String {
        val name = products.fold(Pair(0.0, products[0].name)) { acc, product ->
            if (product.price > acc.first) {
                Pair(product.price, product.name)
            } else {
                acc
            }
        }.second
        return name
    }

    // Check if a particular product name is in stock
    fun isInstock(productName: String): Boolean {
        val instock = products.any {
            it.name.lowercase() == productName.lowercase() && it.quantity > 0
        }
        return instock
    }
}