package com.example.db_api_learning.service

import com.example.db_api_learning.component.sortByMulti
import com.example.db_api_learning.dto.request.CreateSaleRequest
import com.example.db_api_learning.dto.response.*
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.*
import com.example.db_api_learning.model.Sale
import com.example.db_api_learning.model.SaleDetail
import com.example.db_api_learning.repository.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
@Transactional
class OrderService(
    private val saleRepository: SaleRepository,
    private val saleDetailRepository: SaleDetailRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val unitRepository: UnitRepository
) {

    @Transactional(readOnly = true)
    fun getAllOrders(
        currentPage: Int,
        pageSize: Int,
        sortBy: List<String>?
    ): ApiResponse<SaleResponse> {
        val order = sortBy?.sortByMulti()?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage-1, pageSize,sort)

        val sales = saleRepository.findAll(pageable)

        val result = sales.map { sale ->
            // Get sale details for this sale
            val saleDetails = saleDetailRepository.findBySaleId(sale.id)

            // Map each sale detail with its product
            val saleDetailResponses = saleDetails.map { detail ->
                // Get product for this detail
                val product = productRepository.findById(detail.productId.toInt()).orElseThrow {
                    IllegalArgumentException("Product not found: ${detail.productId}")
                }
                val unit = unitRepository.findById(product.unitId).get()
                val category = categoryRepository.findById(product.categoryId).get()

                // Create response with product info
                detail.toSaleResponse().copy(
                    product = product.toProductResponse(unit,category)
                )
            }

            // Return sale response with details
            SaleResponse(
                id = sale.id,
                status = sale.status,
                saleDetail = saleDetailResponses,
                createdAt = sale.createdAt
            )
        }
        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "Success hz",
            ),
            DataResponse(
                content = result.content,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = pageSize,
                    totalElements = result.totalElements,
                    totalPage = result.totalPages,
                )
            )
        )
    }

    fun createOrder(request: CreateSaleRequest): SaleCreateResForm {
        // Validate request
        if (request.items.isEmpty()) {
            throw ResourceNotFoundException("Order must contain at least one item")
        }

        // Get all products id
        val productIds = request.items.map { it.productId.toInt() }

        // Validate all products exist
//        productIds.forEach {
//            productRepository.findById(it).orElseThrow{
//                ResourceNotFoundException("Product not found: $it")
//            }
//        }

        val products = productRepository.findAllById(productIds)
        val productMap = products.associateBy { it.id }

        // Create and save sale first with status(default is 1)
        val sale = Sale(status = request.status?:1)
        val savedSale = saleRepository.save(sale)

        // Create sale details
        val saleDetails = mutableListOf<SaleDetail>()

        request.items.forEach { item ->
            val product = productMap[item.productId]
                ?: throw ResourceNotFoundException("Product not found: ${item.productId}")

            // Validate quantity
            if (item.qty <= 0) {
                throw ResourceNotFoundException("Quantity must be greater than 0")
            }

            // Check stock availability
            if (product.stock < item.qty) {
                throw ResourceNotFoundException(
                    "Insufficient stock for product '${product.name}'. Available: ${product.stock}, Requested: ${item.qty}"
                )
            }

            // Calculate prices

            val unitPrice = item.unitPrice?: product.price//price from request null get price from product table
            val totalPrice = unitPrice.multiply(BigDecimal(item.qty))

            // Create sale detail
            val saleDetail = SaleDetail(
                saleId = savedSale.id,
                productId = product.id,
                qty = item.qty,
                unitPrice = unitPrice,
                totalPrice = totalPrice
            )

            saleDetails.add(saleDetail)

            // Reduce stock
            product.stock -= item.qty
            product.updatedAt = LocalDateTime.now()

        }
        // Save sale details
        val saveDetail = //////1 needed below
        saleDetailRepository.saveAll(saleDetails)

        //convert to saleDetailCreateRes do after save sale details, so it can get id from database
///////1 (short recommend)
        val saleDetailsCreateResponse = saveDetail.map { it.toSaleDetailCreateRes(productRepository.findById(it.productId.toInt()).get()) }
///////Or 2
//     var saleDetailsCreateResponse = listOf<SaleDetailCreateRes>()
//        request.items.forEach { item ->
//            val product = productMap[item.productId]
//                //?: throw ResourceNotFoundException("Product not found: ${item.productId}")
//            saleDetailsCreateResponse = saleDetails.map {
//            it.toSaleDetailCreateRes(product!!) //use (!!) to avoid duplicate👆validation product as the two line up above
//            }
//        }

        // Save updated products
        productRepository.saveAll(products)

        // Build response for create with product info detail
//        val saleDetailResponses = saleDetails.map { detail ->
//            val product = productMap[detail.productId]!!
//            val unit = unitRepository.findById(product.unitId).get()
//            val category = categoryRepository.findById(product.categoryId).get()
//            detail.toSaleResponse().copy(product = product.toProductResponse(unit, category))
//        }
//val result = savedSale.toCreateOrderResponse(saleDetails)
        val result =  savedSale.toCreateOrderResponse(saleDetailsCreateResponse)//.copy(orderDetail = saleDetailsCreateResponse)
        result.createdAt = LocalDateTime.now()
        return SaleCreateResForm(
            Status(
                errorCode = 0,
                errorMessage = "Success hz",
            ),
            result,


        )
    }

}