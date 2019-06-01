/**
 * 
 */
package com.marciobarbosa.apiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marciobarbosa.apiapp.entity.Denuncia;

/***
 * 
 * Created by Márcio Barbosa - email: marciobarbosamobile@gmail.com
 * 01/06/2019
 *
 * */

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

}
