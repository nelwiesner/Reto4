package reto4.reto4.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto4.reto4.modelo.ModeloOrder;
//import reto4.reto4.modelo.ModeloUser;
import reto4.reto4.repositorio.RepositorioOrder;

@Service
public class ServiciosOrder {

    @Autowired
    private RepositorioOrder OrderRepository;

    public List<ModeloOrder> getAll() {
        return OrderRepository.getAll();
    }

    public Optional<ModeloOrder> getOrder(int id) {
        return OrderRepository.getOrder(id);
    }

    public ModeloOrder create(ModeloOrder order) {

        //obtiene el maximo id existente en la coleccion
        Optional<ModeloOrder> orderIdMaxima = OrderRepository.lastUserId();

        //si el id de la orden que se recibe como parametro es nulo, entonces valida el maximo id existente en base de datos
        if (order.getId() == null) {
            //valida el maximo id generado, si no hay ninguno aun el primer id sera 1
            if (orderIdMaxima.isEmpty()) {
                order.setId(1);
            } //si retorna informacion suma 1 al maximo id existente y lo asigna como el codigo de la orden
            else {
                order.setId(orderIdMaxima.get().getId() + 1);
            }
        }

        Optional<ModeloOrder> e = OrderRepository.getOrder(order.getId());
        if (e.isEmpty()) {
            return OrderRepository.create(order);
        } else {
            return order;
        }
    }

    public ModeloOrder update(ModeloOrder order) {

        if (order.getId() != null) {
            Optional<ModeloOrder> orderDb = OrderRepository.getOrder(order.getId());
            if (!orderDb.isEmpty()) {
                if (order.getStatus() != null) {
                    orderDb.get().setStatus(order.getStatus());
                }
                OrderRepository.update(orderDb.get());
                return orderDb.get();
            } else {
                return order;
            }
        } else {
            return order;
        }
    }

    public boolean delete(int id) {
        Boolean aBoolean = getOrder(id).map(order -> {
            OrderRepository.delete(order);
            return true;
        }).orElse(false);
        return aBoolean;
    }

    //Ordenes de pedido asociadas a los asesores de una zona
    public List<ModeloOrder> findByZone(String zona) {
        return OrderRepository.findByZone(zona);
    }
 
    //Ordenes de un asesor
    public List<ModeloOrder> ordersSalesManByID(Integer id){
        return OrderRepository.ordersSalesManByID(id);
    }

    //Ordenes de un asesor x Estado
    public List<ModeloOrder> ordersSalesManByState(String state, Integer id){
        return OrderRepository.ordersSalesManByState(state, id);
    }

    //Ordenes de un asesor x fecha
    public List<ModeloOrder> ordersSalesManByDate(String dateStr, Integer id) {
        return OrderRepository.ordersSalesManByDate(dateStr,id);
    }
    /*
    public List<ModeloOrder> findBySalesMan(Integer id) {
        return OrderRepository.findBySalesMan(id);
    }*/
}
