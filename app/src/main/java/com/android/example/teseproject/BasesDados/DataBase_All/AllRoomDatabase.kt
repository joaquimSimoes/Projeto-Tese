package com.android.example.teseproject.BasesDados.DataBase_All

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [All_Methods_Data::class], version = 2)
abstract class AllRoomDatabase : RoomDatabase(){
    abstract fun allDao(): All_Methods_Dao
}
/*val db = remember {
    Room.databaseBuilder(
        context,
        AllRoomDatabase::class.java,
        "allMethods.db"
    ).fallbackToDestructiveMigration().build()
}
val allDao = db.allDao()
val allUserViewModel: AllUserViewModel = viewModel(
    factory = AllUserViewModelFactory(allDao)
)*/