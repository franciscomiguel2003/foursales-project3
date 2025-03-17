package br.com.foursales.dao;

import br.com.foursales.dto.TicketMedioUsuarioResponseDTO;
import br.com.foursales.dto.Top5UsuariosComprasResponseDTO;
import br.com.foursales.dto.TotalFaturadoMesResponseDTO;
import br.com.foursales.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoDAO extends JpaRepository<PedidoEntity, Long> {


    String query_valot_total_mes = """
            SELECT DATE_FORMAT(p.data_pedido, '%m/%Y') AS mesAno, 
                COALESCE(SUM(p.valor_total), 0) AS totalFaturadoMes 
            FROM pedido p 
              where p.data_pedido >= :dataInicio and  p.data_pedido < :dataFim 
            GROUP BY 
                DATE_FORMAT(p.data_pedido, '%m/%Y') 
            ORDER BY  
                 DATE_FORMAT(p.data_pedido, '%m/%Y') DESC 
            """;

    String query_ticket_medio  = """
            select ROW_NUMBER() OVER (ORDER BY IFNULL(AVG(p.valor_total), 0) desc) AS numeroLinha, 
            u.nome as nome, IFNULL(AVG(p.valor_total), 0) as ticketMedio 
            from user u  
            inner join pedido p on p.id_user = u.id 
            	where p.id_status = 2 and p.valor_total is not null 
            group by  u.username
            order by ticketMedio DESC;      
            """;


     String query_top_5_melhores = """
            select u.nome as nome, sum(p.valor_total) as valorTotalPedidos 
            from user u 
            inner join pedido p on p.id_user = u.id 
            	where p.id_status = 2 
            group by  u.username 
            order by sum(p.valor_total) desc 
            limit 5           
            """;


    @Query(value = query_ticket_medio, nativeQuery = true)
    public List<TicketMedioUsuarioResponseDTO> buscarTicketMedioUsuario();


    @Query(value = query_valot_total_mes, nativeQuery = true)
    public List<TotalFaturadoMesResponseDTO> buscarTotalFaturadoMes(@Param("dataInicio") LocalDate dataInicio,
                                                        @Param("dataFim") LocalDate dataFim);

    @Query(value = query_top_5_melhores, nativeQuery = true)
    public List<Top5UsuariosComprasResponseDTO> buscaTop5MelhoresUsuarios();
}
