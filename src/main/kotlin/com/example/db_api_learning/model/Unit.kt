package com.example.db_api_learning.model
import jakarta.persistence.*

@Entity
@Table(name = "units")
class Unit(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "unit_seq", sequenceName = "units_seq", allocationSize = 20, initialValue = 6)
    var id: Int = 0,
    var name: String? = null,
)