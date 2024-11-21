package com.bits.scalable.controller;

import com.bits.scalable.dto.MenuItemDTO;
import com.bits.scalable.dto.RestaurantDTO;
import com.bits.scalable.model.MenuItem;
import com.bits.scalable.model.Restaurant;
import com.bits.scalable.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Create a new restaurant
    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantRepository.save(restaurant));
    }

    // Get details of a restaurant including its menu
    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        return restaurant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update restaurant details including its menu
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long restaurantId, @RequestBody Restaurant restaurant) {
        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.notFound().build();
        }
        restaurant.setId(restaurantId);
        return ResponseEntity.ok(restaurantRepository.save(restaurant));
    }

    // Add a new menu item to a restaurant's menu
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<Restaurant> addMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItem menuItem) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            restaurant.get().getMenuItems().add(menuItem);
            return ResponseEntity.ok(restaurantRepository.save(restaurant.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Get the menu of a restaurant
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        return restaurant.map(r -> ResponseEntity.ok(r.getMenuItems())).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Delete a restaurant
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long restaurantId) {
        if (restaurantRepository.existsById(restaurantId)) {
            restaurantRepository.deleteById(restaurantId);
            return ResponseEntity.ok("Restaurant deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a menu item from a restaurant
    @DeleteMapping("/{restaurantId}/menu/{menuItemIndex}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long restaurantId, @PathVariable int menuItemIndex) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();

            // Check if the menu item index is valid
            if (menuItemIndex < 0 || menuItemIndex >= restaurant.getMenuItems().size()) {
                return ResponseEntity.badRequest().body("Invalid menu item index.");
            }

            // Remove the menu item at the specified index
            restaurant.getMenuItems().remove(menuItemIndex);
            restaurantRepository.save(restaurant);

            return ResponseEntity.ok("Menu item deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
