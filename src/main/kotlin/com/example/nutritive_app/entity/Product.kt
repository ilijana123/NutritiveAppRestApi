package com.example.nutritive_app.entity

import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @Column(name = "barcode", nullable = false, unique = true)
    var barcode: String? = null,
    @Column(name="name", nullable = false)
    var name: String,
    @Column(name="image_url", nullable = true)
    var image_url: String,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "nutriment_id")
    var nutriments: Nutriment? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "nutriscore_id")
    var nutriscore: Nutriscore? = null,

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    val additives: MutableSet<Additive> = mutableSetOf(),

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    val tags: MutableSet<Tag> = mutableSetOf(),

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    val countries: MutableSet<Country> = mutableSetOf(),

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    val allergens: MutableSet<Allergen> = mutableSetOf(),

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    val categories: MutableSet<Category> = mutableSetOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product
        return barcode == other.barcode
    }

    override fun hashCode(): Int {
        return barcode?.hashCode() ?: 0
    }
}