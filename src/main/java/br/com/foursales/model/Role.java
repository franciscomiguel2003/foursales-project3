package br.com.foursales.model;

public enum Role {
    ADMIN("ADMIN"), USER("USER");

    String role;
    private Role(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }

}