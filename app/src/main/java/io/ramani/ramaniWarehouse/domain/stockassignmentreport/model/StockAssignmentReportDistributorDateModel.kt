package io.ramani.ramaniWarehouse.domain.stockassignmentreport.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

data class StockAssignmentReportDistributorDateModel(
    val id: String = "",
    val assigner: String = "",
    val dateStockTaken: String = "",
    val comment: String = "",
    val companyId: String = "",
    val timestamp: String = "",

    var listOfProducts: List<GoodsReceivedItemModel> = ArrayList(),

    val name: String = "",
    val __v: String = "",
    val salesPersonUID: String = "",

    val stockAssignmentType: String = "",

    val storeKeeperSignature: List<String> = ArrayList(),
    val salesPersonSignature: List<String> = ArrayList(),

    ) : Parcelable {

    class Builder : IBuilder<StockAssignmentReportDistributorDateModel> {
        private var id: String = ""
        private var assigner: String = ""
        private var dateStockTaken: String = ""
        private var comment: String = ""
        private var companyId: String = ""
        private var timestamp: String = ""

        private var listOfProducts: List<GoodsReceivedItemModel> = ArrayList()

        private var name: String = ""
        private var __v: String = ""
        private var salesPersonUID: String = ""
        private var stockAssignmentType: String = ""

        private var storeKeeperSignature: List<String> = ArrayList()
        private var salesPersonSignature: List<String> = ArrayList()

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun companyId(id: String): Builder {
            this.companyId = id
            return this
        }

        fun assigner(supplierName: String): Builder {
            this.assigner = supplierName
            return this
        }

        fun salesPersonUUID(uuid: String): Builder {
            this.salesPersonUID = uuid
            return this
        }

        fun stockAssignmentType(stockAssignmentType: String): Builder {
            this.stockAssignmentType = stockAssignmentType
            return this
        }

        fun dateStockTaken(date: String): Builder {
            this.dateStockTaken = date
            return this
        }

        fun timeStamp(time: String): Builder {
            this.timestamp = time
            return this
        }

        fun listOfProducts(items: List<GoodsReceivedItemModel>): Builder {
            this.listOfProducts = items
            return this
        }


        fun storeKeeperSignature(storeKeeperSignature: List<String>): Builder {
            this.storeKeeperSignature = storeKeeperSignature
            return this
        }

        fun salesPersonSignature(deliveryPersonSignature: List<String>): Builder {
            this.salesPersonSignature = deliveryPersonSignature
            return this
        }

        fun name(deliveryPersonName: String): Builder {
            this.name = deliveryPersonName
            return this
        }

        fun version(version: String): Builder {
            this.__v = version
            return this
        }

        override fun build(): StockAssignmentReportDistributorDateModel =
            StockAssignmentReportDistributorDateModel(
                id,
                assigner,
                dateStockTaken,
                comment,
                companyId,
                timestamp,
                listOfProducts,
                name,
                __v,
                salesPersonUID,
                stockAssignmentType,
                storeKeeperSignature,
                salesPersonSignature
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(GoodsReceivedItemModel) ?: ArrayList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createStringArrayList() ?: ArrayList(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(assigner)
        parcel.writeString(dateStockTaken)
        parcel.writeString(comment)
        parcel.writeString(timestamp)
        parcel.writeTypedList(listOfProducts)
        parcel.writeString(name)
        parcel.writeString(__v)
        parcel.writeString(salesPersonUID)
        parcel.writeString(stockAssignmentType)
        parcel.writeStringList(storeKeeperSignature)
        parcel.writeStringList(salesPersonSignature)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockAssignmentReportDistributorDateModel> {
        override fun createFromParcel(parcel: Parcel): StockAssignmentReportDistributorDateModel {
            return StockAssignmentReportDistributorDateModel(parcel)
        }

        override fun newArray(size: Int): Array<StockAssignmentReportDistributorDateModel?> {
            return arrayOfNulls(size)
        }
    }

}