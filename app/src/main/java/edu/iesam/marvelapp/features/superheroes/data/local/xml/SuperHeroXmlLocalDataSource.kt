package edu.iesam.marvelapp.features.superheroes.data.local.xml

import android.content.Context
import edu.iesam.marvelapp.R
import edu.iesam.marvelapp.core.cache.AppCacheTTL
import edu.iesam.marvelapp.core.serialization.JsonSerializer
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero


/*
* Conector para gestionar datos en un fichero XML a traves de SharedPreferences.
*
*
*/

class SuperHeroXmlLocalDataSource(
    private val context: Context,
    private val serialization: JsonSerializer<List<SuperHero>>,
    private val cacheTTL: AppCacheTTL
) {

    //    Me creo la fuente de datos xml: SharedPreferences
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.local_source_superhero), Context.MODE_PRIVATE
    )

//    Metodo para guardarun listado de superhero

    fun save(superHero: List<SuperHero>) {
        sharedPref.edit().apply {
            putString(keySuperHero, serialization.toJson(superHero))
            apply()

        }
        cacheTTL.updateTime(keySuperHero)

    }

    public fun findAll(): List<SuperHero> {

        if (!cacheTTL.isExpired(keySuperHero, ttl)) {
            val jsonList: String? = sharedPref.getString(keySuperHero, null)

            jsonList?.let {
                serialization.fromJson(it)
            }
        }
        return emptyList()

    }

    fun findById(id: String): SuperHero? {

        return findAll().find { it.id == id }

    }

    fun deleteById(id: String) {

        val superheroList = findAll().filterNot { it.id == id }

        save(superheroList)


    }

    fun deleteAll() {

        sharedPref.edit().apply {
            remove(keySuperHero)
            apply()
        }

    }

    companion object {
        private val keySuperHero: String = "superhero"
        private val ttl: Long = 5 * 60 * 1000

    }


}