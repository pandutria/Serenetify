<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Event extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'organizer',
        'location',
        'price',
        'admin_id',
        'image_url',
        'description',
        'date',
        'time',
        'city',
    ];

    public function admin()
    {
        return $this->belongsTo(User::class, 'admin_id');
    }
}
